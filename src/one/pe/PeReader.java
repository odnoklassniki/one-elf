/*
 * Copyright 2016 Odnoklassniki Ltd, Mail.Ru Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package one.pe;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class PeReader {
    public static final short MACHINE_UNKNOWN = 0;
    public static final short MACHINE_I386    = 0x14c;
    public static final short MACHINE_IA64    = 0x200;
    public static final short MACHINE_AMD64   = (short) 0x8664;
    public static final short MACHINE_ARM     = 0x1c0;
    public static final short MACHINE_THUMB   = 0x1c2;
    public static final short MACHINE_ARMNT   = 0x1c4;
    public static final short MACHINE_ARM64   = (short) 0xaa64;
    public static final short MACHINE_EBC     = 0xebc;

    public static final int IF_RELOCS_STRIPPED         = 0x0001;
    public static final int IF_EXECUTABLE_IMAGE        = 0x0002;
    public static final int IF_LINE_NUMS_STRIPPED      = 0x0004;
    public static final int IF_LOCAL_SYMS_STRIPPED     = 0x0008;
    public static final int IF_AGGRESSIVE_WS_TRIM      = 0x0010;
    public static final int IF_LARGE_ADDRESS_AWARE     = 0x0020;
    public static final int IF_BYTES_REVERSED_LO       = 0x0080;
    public static final int IF_32BIT_MACHINE           = 0x0100;
    public static final int IF_DEBUG_STRIPPED          = 0x0200;
    public static final int IF_REMOVABLE_RUN_FROM_SWAP = 0x0400;
    public static final int IF_NET_RUN_FROM_SWAP       = 0x0800;
    public static final int IF_SYSTEM                  = 0x1000;
    public static final int IF_DLL                     = 0x2000;
    public static final int IF_UP_SYSTEM_ONLY          = 0x4000;
    public static final int IF_BYTES_REVERSED_HI       = 0x8000;

    public static final short IS_UNKNOWN                 = 0;
    public static final short IS_NATIVE                  = 1;
    public static final short IS_WINDOWS_GUI             = 2;
    public static final short IS_WINDOWS_CUI             = 3;
    public static final short IS_POSIX_CUI               = 7;
    public static final short IS_WINDOWS_CE_GUI          = 9;
    public static final short IS_EFI_APPLICATION         = 10;
    public static final short IS_EFI_BOOT_SERVICE_DRIVER = 11;
    public static final short IS_EFI_RUNTIME_DRIVER      = 12;
    public static final short IS_EFI_ROM                 = 13;
    public static final short IS_XBOX                    = 14;

    public static final int IDC_DYNAMIC_BASE          = 0x0040;
    public static final int IDC_FORCE_INTEGRITY       = 0x0080;
    public static final int IDC_NX_COMPAT             = 0x0100;
    public static final int IDC_NO_ISOLATION          = 0x0200;
    public static final int IDC_NO_SEH                = 0x0400;
    public static final int IDC_NO_BIND               = 0x0800;
    public static final int IDC_WDM_DRIVER            = 0x2000;
    public static final int IDC_TERMINAL_SERVER_AWARE = 0x8000;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    final ByteBuffer buf;
    final short machine;
    final short characteristics;
    final int timestamp;
    final boolean pe64;
    final int entryPoint;
    final int baseOfCode;
    final int baseOfData;
    final long imageBase;
    final short subsystem;
    final short dllCharacteristics;
    final PeDirectory[] directories;
    final PeSection[] sections;

    public PeReader(String fileName) throws IOException {
        this(new File(fileName));
    }

    public PeReader(File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        try {
            this.buf = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
        } finally {
            raf.close();
        }

        buf.order(ByteOrder.LITTLE_ENDIAN);

        int p = buf.getInt(60);
        if (buf.get(p) != 'P' || buf.get(p + 1) != 'E' || buf.getShort(p + 2) != 0) {
            throw new PeException("Invalid PE signature");
        }

        p += 4;

        this.machine = buf.getShort(p);
        int numberOfSections = buf.getShort(p + 2) & 0xffff;
        this.timestamp = buf.getInt(p + 4);
        int sizeOfOptionalHeader = buf.getShort(p + 16) & 0xffff;
        this.characteristics = buf.getShort(p + 18);

        p += 20;

        this.pe64 = buf.getShort(p) == 0x20b;
        this.entryPoint = buf.getInt(p + 16);
        this.baseOfCode = buf.getInt(p + 20);
        if (pe64) {
            this.baseOfData = 0;
            this.imageBase = buf.getLong(p + 24);
        } else {
            this.baseOfData = buf.getInt(p + 24);
            this.imageBase = buf.getInt(p + 28) & 0xffffffffL;
        }
        this.subsystem = buf.getShort(p + 68);
        this.dllCharacteristics = buf.getShort(p + 70);

        this.directories = readDirectories(pe64 ? p + 108 : p + 92);
        this.sections = readSections(p + sizeOfOptionalHeader, numberOfSections);
    }

    private PeDirectory[] readDirectories(int offset) {
        PeDirectoryType[] directoryTypes = PeDirectoryType.values();
        int numberOfRvaAndSizes = Math.min(buf.getInt(offset), directoryTypes.length);
        offset += 4;

        PeDirectory[] directories = new PeDirectory[numberOfRvaAndSizes];
        for (int i = 0; i < numberOfRvaAndSizes; i++) {
            directories[i] = directoryTypes[i].instantiate(this, offset);
            offset += 8;
        }
        return directories;
    }

    private PeSection[] readSections(int offset, int numberOfSections) {
        PeSection[] sections = new PeSection[numberOfSections];
        for (int i = 0; i < numberOfSections; i++) {
            sections[i] = new PeSection(this, offset);
            offset += 40;
        }
        return sections;
    }

    String readName(int offset) {
        byte[] bytes = new byte[8];
        for (int i = 0; ; i++) {
            if (i >= 8 || (bytes[i] = buf.get(offset++)) == 0) {
                return new String(bytes, 0, i, UTF8);
            }
        }
    }

    public PeDirectory[] directories() {
        return directories;
    }

    public PeSection[] sections() {
        return sections;
    }
}

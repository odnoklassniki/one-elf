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

import java.nio.ByteBuffer;

public class PeSection {
     public static final int TYPE_NO_PAD            = 0x00000008;
     public static final int CNT_CODE               = 0x00000020;
     public static final int CNT_INITIALIZED_DATA   = 0x00000040;
     public static final int CNT_UNINITIALIZED_DATA = 0x00000080;
     public static final int LNK_OTHER              = 0x00000100;
     public static final int LNK_INFO               = 0x00000200;
     public static final int LNK_REMOVE             = 0x00000800;
     public static final int LNK_COMDAT             = 0x00001000;
     public static final int GPREL                  = 0x00008000;
     public static final int MEM_PURGEABLE          = 0x00020000;
     public static final int MEM_16BIT              = 0x00020000;
     public static final int MEM_LOCKED             = 0x00040000;
     public static final int MEM_PRELOAD            = 0x00080000;
     public static final int ALIGN_1BYTES           = 0x00100000;
     public static final int ALIGN_2BYTES           = 0x00200000;
     public static final int ALIGN_4BYTES           = 0x00300000;
     public static final int ALIGN_8BYTES           = 0x00400000;
     public static final int ALIGN_16BYTES          = 0x00500000;
     public static final int ALIGN_32BYTES          = 0x00600000;
     public static final int ALIGN_64BYTES          = 0x00700000;
     public static final int ALIGN_128BYTES         = 0x00800000;
     public static final int ALIGN_256BYTES         = 0x00900000;
     public static final int ALIGN_512BYTES         = 0x00A00000;
     public static final int ALIGN_1024BYTES        = 0x00B00000;
     public static final int ALIGN_2048BYTES        = 0x00C00000;
     public static final int ALIGN_4096BYTES        = 0x00D00000;
     public static final int ALIGN_8192BYTES        = 0x00E00000;
     public static final int LNK_NRELOC_OVFL        = 0x01000000;
     public static final int MEM_DISCARDABLE        = 0x02000000;
     public static final int MEM_NOT_CACHED         = 0x04000000;
     public static final int MEM_NOT_PAGED          = 0x08000000;
     public static final int MEM_SHARED             = 0x10000000;
     public static final int MEM_EXECUTE            = 0x20000000;
     public static final int MEM_READ               = 0x40000000;
     public static final int MEM_WRITE              = 0x80000000;

    final PeReader reader;
    final String name;
    final int virtualSize;
    final int virtualAddress;
    final int sizeOfRawData;
    final int pointerToRawData;
    final int characteristics;

    PeSection(PeReader reader, int offset) {
        ByteBuffer buf = reader.buf;
        this.reader = reader;
        this.name = reader.readName(offset);
        this.virtualSize = buf.getInt(offset + 8);
        this.virtualAddress = buf.getInt(offset + 12);
        this.sizeOfRawData = buf.getInt(offset + 16);
        this.pointerToRawData = buf.getInt(offset + 20);
        this.characteristics = buf.getInt(offset + 36);
    }

    public String name() {
        return name;
    }

    public int virtualSize() {
        return virtualSize;
    }

    public long virtualAddress() {
        return virtualAddress & 0xffffffffL;
    }

    public int sizeOfRawData() {
        return sizeOfRawData;
    }

    public long pointerToRawData() {
        return pointerToRawData & 0xffffffffL;
    }

    public int characteristics() {
        return characteristics;
    }

    @Override
    public String toString() {
        return name() + '(' + Long.toHexString(virtualAddress()) + ',' + virtualSize() + ')';
    }
}

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

package one.elf;

import java.nio.ByteBuffer;

public class ElfSymbol {
    public static final byte STB_LOCAL   = 0;
    public static final byte STB_GLOBAL  = 1;
    public static final byte STB_WEAK    = 2;
    public static final byte STB_LOPROC  = 13;
    public static final byte STB_HIPROC  = 15;

    public static final byte STT_NOTYPE  = 0;
    public static final byte STT_OBJECT  = 1;
    public static final byte STT_FUNC    = 2;
    public static final byte STT_SECTION = 3;
    public static final byte STT_FILE    = 4;
    public static final byte STT_LOPROC  = 13;
    public static final byte STT_HIPROC  = 15;

    final ElfReader reader;
    final ElfStringTable strtab;
    final int nameIndex;
    final int info;
    final byte other;
    final int sectionIndex;
    final long value;
    final long size;

    ElfSymbol(ElfReader reader, ElfStringTable strtab, int offset) {
        ByteBuffer buf = reader.buf;
        this.reader = reader;
        this.strtab = strtab;
        this.nameIndex = buf.getInt(offset);

        if (reader.elf64) {
            this.info = buf.get(offset + 4) & 0xff;
            this.other = buf.get(offset + 5);
            this.sectionIndex = buf.getShort(offset + 6) & 0xffff;
            this.value = buf.getLong(offset + 8);
            this.size = buf.getInt(offset + 16);
        } else {
            this.value = buf.getInt(offset + 4) & 0xffffffffL;
            this.size = buf.getInt(offset + 8) & 0xffffffffL;
            this.info = buf.get(offset + 12) & 0xff;
            this.other = buf.get(offset + 13);
            this.sectionIndex = buf.getShort(offset + 14) & 0xffff;
        }
    }

    public String name() {
        return strtab == null ? null : strtab.string(nameIndex);
    }

    public long value() {
        return value;
    }

    public long size() {
        return size;
    }

    public byte bind() {
        return (byte) (info >>> 4);
    }

    public byte type() {
        return (byte) (info & 0xf);
    }

    public byte other() {
        return other;
    }

    public ElfSection section() {
        return reader.sections[sectionIndex];
    }

    @Override
    public String toString() {
        return name() + '@' + Long.toHexString(value);
    }
}

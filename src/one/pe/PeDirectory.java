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

public class PeDirectory {
    final PeReader reader;
    final PeDirectoryType type;
    final int virtualAddress;
    final int size;

    PeDirectory(PeReader reader, PeDirectoryType type, int offset) {
        ByteBuffer buf = reader.buf;
        this.reader = reader;
        this.type = type;
        this.virtualAddress = buf.getInt(offset);
        this.size = buf.getInt(offset + 4);
    }

    public PeDirectoryType type() {
        return type;
    }

    public long virtualAddress() {
        return virtualAddress & 0xffffffffL;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return virtualAddress == 0;
    }

    @Override
    public String toString() {
        return type().name() + '(' + Long.toHexString(virtualAddress()) + ',' + size() + ')';
    }
}

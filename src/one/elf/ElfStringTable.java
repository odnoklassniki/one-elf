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

public class ElfStringTable extends ElfSection {

    ElfStringTable(ElfReader reader, int offset) {
        super(reader, offset);
    }

    public String string(int index) {
        ByteBuffer buf = reader.buf;
        int pos = (int) offset + index;

        StringBuilder result = new StringBuilder();
        for (byte b; (b = buf.get(pos)) != 0; pos++) {
            result.append((char) b);
        }
        return result.toString();
    }
}

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

import java.util.Iterator;

public class ElfRelocationTable extends ElfSection implements Iterable<ElfRelocation> {

    ElfRelocationTable(ElfReader reader, int offset) {
        super(reader, offset);
    }

    public ElfRelocation relocation(int index) {
        return new ElfRelocation(reader, this, (int) (offset + index * entrySize));
    }

    public ElfRelocation[] relocations() {
        ElfRelocation[] relocations = new ElfRelocation[count()];
        for (int i = 0; i < relocations.length; i++) {
            relocations[i] = relocation(i);
        }
        return relocations;
    }

    @Override
    public Iterator<ElfRelocation> iterator() {
        return new Iterator<ElfRelocation>() {
            final int count = count();
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public ElfRelocation next() {
                return relocation(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

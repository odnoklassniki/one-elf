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

public class ElfSymbolTable extends ElfSection implements Iterable<ElfSymbol> {

    ElfSymbolTable(ElfReader reader, int offset) {
        super(reader, offset);
    }

    public ElfSymbol symbol(int index) {
        return new ElfSymbol(reader, (ElfStringTable) link(), (int) (offset + index * entrySize));
    }

    public ElfSymbol symbol(String name) {
        for (ElfSymbol symbol : this) {
            if (name.equals(symbol.name())) {
                return symbol;
            }
        }
        return null;
    }

    public ElfSymbol[] symbols() {
        ElfSymbol[] symbols = new ElfSymbol[count()];
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = symbol(i);
        }
        return symbols;
    }

    @Override
    public Iterator<ElfSymbol> iterator() {
        return new Iterator<ElfSymbol>() {
            private final int count = count();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public ElfSymbol next() {
                return symbol(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

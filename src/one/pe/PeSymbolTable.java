package one.pe;

import java.util.Iterator;

public class PeSymbolTable implements Iterable<PeSymbol> {
    final PeReader reader;
    final int offset;
    final int count;

    PeSymbolTable(PeReader reader, int offset, int count) {
        this.reader = reader;
        this.offset = offset;
        this.count = count;
    }

    public int count() {
        return count;
    }

    public PeSymbol symbol(int index) {
        return new PeSymbol(reader, offset + index * 18);
    }

    public PeSymbol symbol(String name) {
        for (PeSymbol symbol : this) {
            if (name.equals(symbol.name())) {
                return symbol;
            }
        }
        return null;
    }

    public PeSymbol[] symbols() {
        PeSymbol[] symbols = new PeSymbol[count()];
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = symbol(i);
        }
        return symbols;
    }

    @Override
    public Iterator<PeSymbol> iterator() {
        return new Iterator<PeSymbol>() {
            private final int count = count();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public PeSymbol next() {
                return symbol(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

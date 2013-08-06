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

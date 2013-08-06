package one.pe;

import java.nio.ByteBuffer;

public class PeSymbol {
    public static final short IMAGE_SYM_UNDEFINED =  0;
    public static final short IMAGE_SYM_ABSOLUTE  = -1;
    public static final short IMAGE_SYM_DEBUG     = -2;

    final PeReader reader;
    final String name;
    final int value;
    final short section;
    final short type;
    final byte storageClass;
    final byte numberOfAuxSymbols;

    PeSymbol(PeReader reader, int offset) {
        ByteBuffer buf = reader.buf;
        this.reader = reader;
        this.name = reader.readName(offset);
        this.value = buf.getInt(offset + 8);
        this.section = buf.getShort(offset + 12);
        this.type = buf.getShort(offset + 14);
        this.storageClass = buf.get(offset + 16);
        this.numberOfAuxSymbols = buf.get(offset + 17);
    }

    public String name() {
        return name;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return name() + '@' + Long.toHexString(value);
    }
}

package one.pe;

public class PeReaderTest {

    public static void main(String[] args) throws Exception {
        PeReader reader = new PeReader(args[0]);

        System.out.println("Sections:");
        for (PeSection section : reader.sections()) {
            System.out.println("  " + section);
        }

        System.out.println("Symbols:");
        PeSymbolTable symtab = reader.symtab();
        for (PeSymbol symbol : symtab) {
            System.out.println("  " + symbol);
        }
    }
}

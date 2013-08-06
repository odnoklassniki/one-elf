package one.pe;

public class PeReaderTest {

    public static void main(String[] args) throws Exception {
        PeReader reader = new PeReader(args[0]);

        System.out.println("Data directories:");
        for (PeDirectory directory : reader.directories()) {
            System.out.println("  " + directory);
        }

        System.out.println("Sections:");
        for (PeSection section : reader.sections()) {
            System.out.println("  " + section);
        }

    }
}

package one.pe;

public enum PeDirectoryType {
    ExportTable,
    ImportTable,
    ResourceTable,
    ExceptionTable,
    CertificateTable,
    BaseRelocationTable,
    Debug,
    Architecture,
    GlobalPtr,
    TLSTable,
    LoadConfigTable,
    BoundImport,
    ImportAddressTable,
    DelayImportDescriptor,
    CLRRuntimeHeader,
    Reserved;

    PeDirectory instantiate(PeReader reader, int offset) {
        return new PeDirectory(reader, this, offset);
    }
}

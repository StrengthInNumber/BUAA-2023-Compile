package Backend.InstrAsm;

public class AsmInstrSpace extends AsmInstr {
    private String name;
    private int size;

    public AsmInstrSpace(String name, int size) {
        super(AsmInstrOp.SPACE);
        this.name = name;
        this.size = size;
    }

    @Override
    public String toString() {
        return name + ": .space " + size;
    }
}

package Backend.InstrAsm;

public class AsmInstrWord extends AsmInstr {
    private String name;
    private int value;

    public AsmInstrWord(String name, int value) {
        super(AsmInstrOp.WORD);
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + ": .word " + value;
    }
}

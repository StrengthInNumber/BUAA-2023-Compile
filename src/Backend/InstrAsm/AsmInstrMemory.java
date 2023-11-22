package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrMemory extends AsmInstr {
    private Register rd;
    private Register base;
    private int offset;
    private String label;

    public AsmInstrMemory(AsmInstrOp op, Register rd, Register base, Integer offset) {
        super(op);
        this.rd = rd;
        this.base = base;
        this.offset = offset;
        this.label = null;
    }

    public AsmInstrMemory(AsmInstrOp op, Register rd, String label, Integer offset) {
        super(op);
        this.rd = rd;
        this.base = null;
        this.offset = offset;
        this.label = label;
    }

    @Override
    public String toString() {
        if (label == null) {
            return op + " " + rd + " " + offset + "(" + base + ")";
        } else {
            return op + " " + rd + " " + label + "+" + offset;
        }
    }
}

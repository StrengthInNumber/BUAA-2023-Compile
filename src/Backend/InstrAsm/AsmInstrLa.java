package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrLa extends AsmInstr{
    private Register rd;
    private String label;

    public AsmInstrLa(AsmInstrOp op, Register rd, String label) {
        super(op);
        assert op == AsmInstrOp.LA;
        this.rd = rd;
        this.label = label;
    }

    @Override
    public String toString() {
        return "la " + rd + " " + label;
    }
}

package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrLa extends AsmInstr{
    private Register rd;
    private String label;

    public AsmInstrLa(Register rd, String label) {
        super(AsmInstrOp.LA, 2);
        this.rd = rd;
        this.label = label;
    }

    @Override
    public String toString() {
        return "la " + rd + " " + label;
    }
}

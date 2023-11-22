package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrLi extends AsmInstr {
    private Register rd;
    private int number;

    public AsmInstrLi(AsmInstrOp op, Register rd, Integer number) {
        super(op);
        assert op == AsmInstrOp.LI;
        this.rd = rd;
        this.number = number;
    }

    @Override
    public String toString() {
        return "li " + rd + " " + number;
    }
}

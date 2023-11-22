package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrHiLo extends AsmInstr {
    private AsmInstrOp op;
    private Register rd;

    public AsmInstrHiLo(AsmInstrOp op, Register rd) {
        super(op);
        this.rd = rd;
    }

    @Override
    public String toString() {
        return op + " " + rd;
    }
}

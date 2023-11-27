package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrHiLo extends AsmInstr {
    private Register rd;

    public AsmInstrHiLo(AsmInstrOp op, Register rd) {
        super(op, 2);
        this.rd = rd;
    }

    @Override
    public String toString() {
        return op + " " + rd;
    }
}

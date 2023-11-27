package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrCmp extends AsmInstr {
    private Register rd;
    private Register rs;
    private Register rt;

    public AsmInstrCmp(AsmInstrOp op, Register rd, Register rs, Register rt) {
        super(op, 2);
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return op + " " + rd + " " + rs + " " + rt;
    }
}

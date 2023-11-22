package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrCmp {
    private AsmInstrOp op;
    private Register rd;
    private Register rs;
    private Register rt;

    public AsmInstrCmp(AsmInstrOp op, Register rd, Register rs, Register rt) {
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return op + " " + rd + " " + rs + " " + rt;
    }

}

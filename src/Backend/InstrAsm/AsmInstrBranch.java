package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrBranch extends AsmInstr{
    private Register rs;
    private Register rt;
    private String label;

    public AsmInstrBranch(AsmInstrOp op, Register rs, Register rt, String label) {
        super(op,2);
        this.rs = rs;
        this.rt = rt;
        this.label = label;
    }

    @Override
    public String toString() {
        return op + " " + rs + " " + rt + " " + label;
    }
}

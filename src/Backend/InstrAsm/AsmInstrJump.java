package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrJump extends AsmInstr {
    private String target;
    private Register rd;

    public AsmInstrJump(AsmInstrOp op, String target) {
        super(op, 2);
        this.target = target;
        this.rd = null;
    }


    public AsmInstrJump(AsmInstrOp op, Register rd) {
        super(op, 2);
        this.target = null;
        this.rd = rd;
    }

    @Override
    public String toString() {
        if (target == null) {
            return op + " " + rd;
        } else {
            return op + " " + target;
        }
    }
}

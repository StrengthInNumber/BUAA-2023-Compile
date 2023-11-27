package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrMove extends AsmInstr {
    private Register dst;
    private Register src;

    public AsmInstrMove(Register dst, Register src) {
        super(AsmInstrOp.MOVE, 2);
        this.dst = dst;
        this.src = src;
    }

    @Override
    public String toString() {
        return "move " + dst + " " + src;
    }
}

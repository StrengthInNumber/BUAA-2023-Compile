package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrMove {
    private Register dst;
    private Register src;

    public AsmInstrMove(Register dst, Register src) {
        this.dst = dst;
        this.src = src;
    }

    @Override
    public String toString() {
        return "move " + dst + " " + src;
    }
}

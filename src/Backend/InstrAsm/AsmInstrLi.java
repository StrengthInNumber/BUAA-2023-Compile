package Backend.InstrAsm;

import Backend.Register;

public class AsmInstrLi extends AsmInstr {
    private Register rd;
    private int number;

    public AsmInstrLi(Register rd, Integer number) {
        super(AsmInstrOp.LI, 2);
        this.rd = rd;
        this.number = number;
    }

    @Override
    public String toString() {
        return "li " + rd + " " + number;
    }
}

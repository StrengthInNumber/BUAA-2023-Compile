package Backend.InstrAsm;

import Backend.Register;

import java.util.Objects;

public class AsmInstrAlu extends AsmInstr {
    private Register rd;
    private Register rs;
    private Register rt;
    private int number;

    public AsmInstrAlu(AsmInstrOp op, Register rd, Register rs, Register rt) {
        super(op);
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
        this.number = -99999;
    }

    public AsmInstrAlu(AsmInstrOp op, Register rd, Register rs, int number) {
        super(op);
        this.rd = rd;
        this.rs = rs;
        this.rt = null;
        this.number = number;
    }

    public String toString() {
        return op + " " + rd + " " + rs + " " + Objects.requireNonNullElseGet(rt, () -> number);
    }
}

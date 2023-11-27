package Backend.InstrAsm;

import Backend.Register;

import java.util.Objects;

public class AsmInstrAlu extends AsmInstr {
    private Register rd;
    private Register rs;
    private Register rt;
    private int number;

    public AsmInstrAlu(AsmInstrOp op, Register rd, Register rs, Register rt) {
        super(op, 2);
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
        this.number = -99999;
    }

    public AsmInstrAlu(AsmInstrOp op, Register rd, Register rs, int number) {
        super(op, 2);
        this.rd = rd;
        this.rs = rs;
        this.rt = null;
        this.number = number;
    }

    public AsmInstrAlu(AsmInstrOp op, Register rs, Register rt) {
        super(op, 2);
        this.rd = null;
        this.rs = rs;
        this.rt = rt;
    }

    public String toString() {
        if(rd == null) {
            return op + " " + rs + " " + rt;
        } else if(rt == null) {
            return op + " " + rd + " " + rs + " " + number;
        } else {
            return op + " " + rd + " " + rs + " " + rt;
        }
    }
}

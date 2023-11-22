package Backend.InstrAsm;

public class AsmInstrSyscall extends AsmInstr {
    private AsmInstrOp op;
    public AsmInstrSyscall(AsmInstrOp op) {
        super(op);
    }
}

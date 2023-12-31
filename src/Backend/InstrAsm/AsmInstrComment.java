package Backend.InstrAsm;

public class AsmInstrComment extends AsmInstr {
    private String content;

    public AsmInstrComment(AsmInstrOp op, String content) {
        super(op, 2);
        this.content = content;
    }

    public String toString() {
        return '#' + content;
    }

}

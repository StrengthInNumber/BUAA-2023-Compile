package Backend.InstrAsm;

public class AsmComment extends AsmInstr {
    private String content;
    public AsmComment(String content) {
        super(AsmInstrOp.COMMENT, 2);
        this.content = content;
    }

    public String toString() {
        return '#' + content;
    }
}

package Backend.InstrAsm;

public class AsmInstrLabel extends AsmInstr{
    private String labelName;

    public AsmInstrLabel(String labelName) {
        super(AsmInstrOp.LABEL, 2);
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return labelName + ":";
    }
}

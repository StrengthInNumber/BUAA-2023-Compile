package Backend.InstrAsm;

public class AsmInstrLabel extends AsmInstr{
    private String label;

    public AsmInstrLabel(AsmInstrOp op, String label) {
        super(op);
        this.label = label;
    }

    @Override
    public String toString() {
        return label + ":";
    }
}

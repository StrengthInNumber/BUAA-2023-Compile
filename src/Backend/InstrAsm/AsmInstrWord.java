package Backend.InstrAsm;

import java.util.ArrayList;

public class AsmInstrWord extends AsmInstr {
    private String name;
    private int value;
    private ArrayList<Integer> values;
    private boolean isArray;

    public AsmInstrWord(String name, int value) {
        super(AsmInstrOp.WORD, 1);
        this.name = name;
        this.value = value;
        isArray = false;
        values = null;
    }

    public AsmInstrWord(String name, ArrayList<Integer> values) {
        super(AsmInstrOp.WORD, 1);
        this.name = name;
        isArray = true;
        this.values = values;
    }


    @Override
    public String toString() {
        if (isArray) {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(": .word ");
            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i)).append(',');
            }
            return sb.toString();
        } else {
            return name + ": .word " + value;
        }
    }
}

package Middle.LLVMIR.Constant;

import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;

public class IRConstArray extends IRConst {
    private ArrayList<IRConstInt> ints;

    public IRConstArray(IRType type, ArrayList<IRConstInt> ints) {
        super(type);
        this.ints = ints;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ints.get(0).getType());
        sb.append(' ');
        sb.append(ints.get(0));
        for (int i = 1; i < ints.size(); i++) {
            sb.append(", ");
            sb.append(ints.get(i).getType());
            sb.append(' ');
            sb.append(ints.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}

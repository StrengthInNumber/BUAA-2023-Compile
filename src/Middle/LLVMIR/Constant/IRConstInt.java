package Middle.LLVMIR.Constant;

import Middle.LLVMIR.Type.IRType;

public class IRConstInt extends IRConst {
    private int val;
    public IRConstInt(IRType type, int val){
        super(String.valueOf(val), type);
        this.val = val;
    }

    public String toString() {
        return String.valueOf(val);
    }
}

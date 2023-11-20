package Middle.LLVMIR.Constant;

import Middle.LLVMIR.IRUser;
import Middle.LLVMIR.Type.IRType;

public class IRConst extends IRUser {
    public IRConst(IRType type) {
        super(type);
    }

    public IRConst(String name, IRType type) {
        super(name, type);
    }
}

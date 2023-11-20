package Middle.LLVMIR.Function;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRType;

public class IRParam extends IRValue {
    public IRParam(String name, IRType type) {
        super(name, type);
    }

    public String toString() {
        return type + " " + name;
    }
}

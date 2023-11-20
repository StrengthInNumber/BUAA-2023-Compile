package Middle.LLVMIR.GlobalVar;

import Middle.LLVMIR.Constant.IRConst;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRUser;
import Middle.LLVMIR.Type.IRPointerType;
import Middle.LLVMIR.Type.IRType;

public class IRGlobalVar extends IRUser {
    private boolean isConst;
    private IRConst init;
    private IRType contentType;
    private boolean isZeroInitializer;

    public IRGlobalVar(String name, IRType type, boolean isConst, IRConst init,
                       boolean ifAutoAdd, boolean isZeroInitializer){
        super(name, new IRPointerType(type));
        this.contentType = type;
        this.isConst = isConst;
        this.init = init;
        this.isZeroInitializer = isZeroInitializer;
        if(ifAutoAdd) {
            IRBuilder.getInstance().addGlobalVar(this);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = dso_local global ")
                .append(contentType.toString()).append(" ");
        if(isZeroInitializer) {
            sb.append("zeroinitializer\n");
        } else {
            sb.append(init.toString()).append('\n');
        }
        return sb.toString();
    }
}

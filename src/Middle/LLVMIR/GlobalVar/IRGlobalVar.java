package Middle.LLVMIR.GlobalVar;

import Backend.InstrAsm.AsmInstrWord;
import Middle.LLVMIR.Constant.IRConst;
import Middle.LLVMIR.Constant.IRConstArray;
import Middle.LLVMIR.Constant.IRConstInt;
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

    public void generateAsm() {
        if(init instanceof IRConstInt) {
            new AsmInstrWord(name.substring(1), ((IRConstInt) init).getVal());
        } else {
            new AsmInstrWord(name.substring(1), ((IRConstArray) init).getVal());
        }
    }
}

package Middle.LLVMIR.Instruction.IOInstr;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrPutInt extends IRInstr {
    public IRInstrPutInt(IRValue source, boolean autoInsert) {
        super(IRInstrType.PUTINT, IRVoidType.VOID, autoInsert);
        operands.add(source);
    }

    public String toString() {
        return "call void @putint(i32 " + operands.get(0).getName() + ")";
    }
}

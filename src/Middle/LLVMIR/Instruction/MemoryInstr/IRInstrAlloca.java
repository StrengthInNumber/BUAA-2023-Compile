package Middle.LLVMIR.Instruction.MemoryInstr;

import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRPointerType;
import Middle.LLVMIR.Type.IRType;

public class IRInstrAlloca extends IRInstr {
    private IRType targetType;
    public IRInstrAlloca(String name, IRType targetType, boolean autoInsert) {
        super(name, IRInstrType.ALLOCA, new IRPointerType(targetType), autoInsert);
        this.targetType= targetType;
    }

    public String toString() {
        return name + " = alloca " + targetType;
    }

}

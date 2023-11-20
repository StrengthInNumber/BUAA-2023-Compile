package Middle.LLVMIR.Instruction.MemoryInstr;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrStore extends IRInstr {
    public IRInstrStore(IRValue source, IRValue dest, boolean autoInsert) {
        super(IRInstrType.STORE, IRVoidType.VOID, autoInsert);
        operands.add(source);
        operands.add(dest);
    }

    public String toString() {
        return "store " +
                operands.get(0).getType() + " " +
                operands.get(0).getName() + ", " +
                operands.get(1).getType() + " " +
                operands.get(1).getName();
    }
}

package Middle.LLVMIR.Instruction.MemoryInstr;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRPointerType;

public class IRInstrLoad extends IRInstr {
    public IRInstrLoad(String name, IRValue pointer, boolean autoInsert) {
        super(name, IRInstrType.LOAD, ((IRPointerType)pointer.getType()).getContentType(), autoInsert);
        operands.add(pointer);
    }

    public String toString() {
        return name + " = load "
                + type + ", "
                + operands.get(0).getType() + " "
                + operands.get(0).getName();
    }
}

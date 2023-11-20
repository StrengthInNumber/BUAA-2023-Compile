package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRArrayType;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRPointerType;
import Middle.LLVMIR.Type.IRType;

public class IRInstrGEP extends IRInstr {
    public IRInstrGEP(String name, IRValue ptr, IRValue offset) {
        super(name, IRInstrType.GEP, new IRPointerType(IRIntegerType.INT32), true);
        operands.add(ptr);
        operands.add(offset);
    }

    public String toString() {
        IRValue pointer = operands.get(0);
        IRPointerType pointerType = (IRPointerType) pointer.getType();
        IRType contentType = pointerType.getContentType();
        IRValue offset = operands.get(1);
        if (contentType instanceof IRArrayType) { //数组
            return name + " = getelementptr inbounds " + contentType + ", " +
                    pointerType + " " + pointer.getName() + ", i32 0, i32 " + offset.getName();
        } else { //
            return name + " = getelementptr inbounds " + contentType + ", " +
                    pointerType + " " + pointer.getName() + ", i32 " + offset.getName();
        }
    }
}

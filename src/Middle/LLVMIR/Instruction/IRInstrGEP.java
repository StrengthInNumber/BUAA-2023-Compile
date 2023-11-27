package Middle.LLVMIR.Instruction;

import Backend.AsmBuilder;
import Backend.InstrAsm.*;
import Backend.Register;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.GlobalVar.IRGlobalVar;
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

    public void generateAsm() {
        new AsmComment(this.toString());
        AsmBuilder.getInstance().asmGetPointer(operands.get(0), Register.K0);
        AsmBuilder.getInstance().asmGetOperand(operands.get(1), Register.K1);
        new AsmInstrAlu(AsmInstrOp.SLL, Register.K1, Register.K1, 2); // *4
        new AsmInstrAlu(AsmInstrOp.ADDU, Register.K1, Register.K1, Register.K0);
        new AsmInstrMemory(AsmInstrOp.SW, Register.K1, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

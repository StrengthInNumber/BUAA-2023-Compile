package Middle.LLVMIR.Instruction.MemoryInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.*;
import Backend.Register;
import Middle.LLVMIR.GlobalVar.IRGlobalVar;
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

    public void generateAsm() {
        new AsmComment(this.toString());
        AsmBuilder.getInstance().asmGetPointer(operands.get(0), Register.K0);
        new AsmInstrMemory(AsmInstrOp.LW, Register.K0, Register.K0, 0);
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

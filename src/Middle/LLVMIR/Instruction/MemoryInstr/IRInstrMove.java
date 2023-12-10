package Middle.LLVMIR.Instruction.MemoryInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrMove extends IRInstr {
    public IRInstrMove(IRValue to, IRValue from, boolean autoInsert) {
        super(IRInstrType.MOVE,IRVoidType.VOID, autoInsert);
        addUseValue(to);
        addUseValue(from);
    }

    public IRValue getToValue() {
        return useValues.get(0);
    }

    public IRValue getFromValue() {
        return useValues.get(1);
    }

    public void setFromValue(IRValue newValue) {
        useValues.set(1, newValue);
    }
    public String toString() {
        return "move " + useValues.get(0).getType() + " " + useValues.get(0).getName()
                + ", " + useValues.get(1).getName();
    }

    public void generateAsm() {
        AsmBuilder.getInstance().asmGetOperand(useValues.get(1), Register.K0);
        //TODO:若 to 分配了寄存器，则对其寄存器赋值
        //如果没有
        Integer offset = AsmBuilder.getInstance().getOffsetOnStack(useValues.get(0));
        if(offset == null) {
            offset = AsmBuilder.getInstance().pushToStack(useValues.get(0), 4);
        }
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP, offset);
    }
}

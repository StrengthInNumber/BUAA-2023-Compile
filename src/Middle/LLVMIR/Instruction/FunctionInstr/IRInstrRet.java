package Middle.LLVMIR.Instruction.FunctionInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrRet extends IRInstr {
    public IRInstrRet(IRValue returnValue, boolean autoInsert) {
        super(IRInstrType.RET, IRVoidType.VOID, autoInsert);
        if(returnValue != null) {
            operands.add(returnValue);
        }
    }

    public String toString() {
        if(operands.isEmpty()) {
            return "ret void";
        } else {
            return "ret " + operands.get(0).getType() + " " + operands.get(0).getName();
        }
    }

    @Override
    public void generateAsm() {
        if(!operands.isEmpty()) {
            AsmBuilder.getInstance().asmGetOperand(operands.get(0), Register.V0);
        }
        new AsmInstrJump(AsmInstrOp.JR, Register.RA);
    }
}

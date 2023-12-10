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
            addUseValue(returnValue);
        }
    }

    public String toString() {
        if(useValues.isEmpty()) {
            return "ret void";
        } else {
            return "ret " + useValues.get(0).getType() + " " + useValues.get(0).getName();
        }
    }

    @Override
    public void generateAsm() {
        if(!useValues.isEmpty()) {
            AsmBuilder.getInstance().asmGetOperand(useValues.get(0), Register.V0);
        }
        new AsmInstrJump(AsmInstrOp.JR, Register.RA);
    }
}

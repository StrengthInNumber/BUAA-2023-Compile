package Middle.LLVMIR.Instruction.IOInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrLi;
import Backend.InstrAsm.AsmInstrOp;
import Backend.InstrAsm.AsmInstrSyscall;
import Backend.Register;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrPutInt extends IRInstr {
    public IRInstrPutInt(IRValue source, boolean autoInsert) {
        super(IRInstrType.PUTINT, IRVoidType.VOID, autoInsert);
        addUseValue(source);
    }

    public String toString() {
        return "call void @putint(i32 " + useValues.get(0).getName() + ")";
    }

    public void generateAsm() {
        AsmBuilder.getInstance().asmGetOperand(useValues.get(0), Register.A0);
        new AsmInstrLi(Register.V0, 1);
        new AsmInstrSyscall(AsmInstrOp.SYSCALL);
    }
}

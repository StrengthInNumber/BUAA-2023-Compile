package Middle.LLVMIR.Instruction.IOInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrLi;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.InstrAsm.AsmInstrSyscall;
import Backend.Register;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRIntegerType;

public class IRInstrGetint extends IRInstr {
    public IRInstrGetint(String name) {
        super(name, IRInstrType.GETINT, IRIntegerType.INT32, true);
    }

    public String toString() {
        return name + " = call i32 @getint()";
    }

    public void generateAsm() {
        new AsmInstrLi(Register.V0, 5);
        new AsmInstrSyscall(AsmInstrOp.SYSCALL);
        new AsmInstrMemory(AsmInstrOp.SW, Register.V0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

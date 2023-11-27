package Middle.LLVMIR.Instruction.IOInstr;

import Backend.InstrAsm.AsmInstrLa;
import Backend.InstrAsm.AsmInstrLi;
import Backend.InstrAsm.AsmInstrOp;
import Backend.InstrAsm.AsmInstrSyscall;
import Backend.Register;
import Middle.LLVMIR.IRString;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrPutCh extends IRInstr {
    private char source;
    private IRString irString;
    private boolean firstInIRString;
    public IRInstrPutCh(char source, boolean autoInsert, IRString irString) {
        super(IRInstrType.PUTCH, IRVoidType.VOID, autoInsert);
        this.source = source;
        this.irString = irString;
        this.firstInIRString = irString.isFirstChar();
    }

    public String toString() {
        return "call void @putch(i32 " + (int)source + ")";
    }

    public void generateAsm() {
        if(firstInIRString) {
            new AsmInstrLa(Register.A0, irString.getName());
            new AsmInstrLi(Register.V0, 4);
            new AsmInstrSyscall(AsmInstrOp.SYSCALL);
        }
    }
}

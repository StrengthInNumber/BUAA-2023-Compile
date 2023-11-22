package Middle.LLVMIR.Instruction.IOInstr;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrPutCh extends IRInstr {
    private char source;
    private
    public IRInstrPutCh(char source, boolean autoInsert) {
        super(IRInstrType.PUTCH, IRVoidType.VOID, autoInsert);
        this.source = source;
    }

    public String toString() {
        return "call void @putch(i32 " + (int)source + ")";
    }
}

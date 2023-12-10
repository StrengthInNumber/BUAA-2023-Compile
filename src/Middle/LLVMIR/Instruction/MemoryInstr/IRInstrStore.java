package Middle.LLVMIR.Instruction.MemoryInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmComment;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrStore extends IRInstr {
    public IRInstrStore(IRValue source, IRValue dest, boolean autoInsert) {
        super(IRInstrType.STORE, IRVoidType.VOID, autoInsert);
        addUseValue(source);
        addUseValue(dest);
    }

    public String toString() {
        return "store " +
                useValues.get(0).getType() + " " +
                useValues.get(0).getName() + ", " +
                useValues.get(1).getType() + " " +
                useValues.get(1).getName();
    }

    @Override
    public void generateAsm() {
        new AsmComment(this.toString());
        AsmBuilder.getInstance().asmGetPointer(useValues.get(1), Register.K1);
        AsmBuilder.getInstance().asmGetOperand(useValues.get(0), Register.K0);
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.K1, 0);
    }

    public IRValue getSource() {
        return useValues.get(0);
    }
}

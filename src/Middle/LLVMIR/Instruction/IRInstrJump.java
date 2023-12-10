package Middle.LLVMIR.Instruction;

import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrOp;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrJump extends IRInstr {
    public IRInstrJump(IRBasicBlock target, boolean autoInsert) {
        super(IRInstrType.JUMP, IRVoidType.VOID, autoInsert);
        addUseValue(target);
    }

    public String toString() {
        return "br label %" + useValues.get(0).getName();
    }

    @Override
    public void generateAsm() {
        new AsmInstrJump(AsmInstrOp.J, useValues.get(0).getName());
    }

    public IRBasicBlock getTargetBB() {
        return (IRBasicBlock) useValues.get(0);
    }
}

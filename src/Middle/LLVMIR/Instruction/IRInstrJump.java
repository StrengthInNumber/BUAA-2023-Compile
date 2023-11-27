package Middle.LLVMIR.Instruction;

import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrOp;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

public class IRInstrJump extends IRInstr {
    public IRInstrJump(IRBasicBlock target, boolean autoInsert) {
        super(IRInstrType.JUMP, IRVoidType.VOID, autoInsert);
        operands.add(target);
    }

    public String toString() {
        return "br label %" + operands.get(0).getName();
    }

    @Override
    public void generateAsm() {
        new AsmInstrJump(AsmInstrOp.J, operands.get(0).getName());
    }
}

package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRIntegerType;

public class IRInstrBranch extends IRInstr {
    public IRInstrBranch(IRValue cond, IRBasicBlock trueBB, IRBasicBlock falseBB, boolean autoInsert) {
        super(IRInstrType.BRANCH, IRIntegerType.INT1, autoInsert);
        operands.add(cond);
        operands.add(trueBB);
        operands.add(falseBB);
    }

    public String toString() {
        return "br " + type + ' ' + operands.get(0).getName()
                + ", label %" + operands.get(1).getName()
                + ", label %" + operands.get(2).getName();
    }
}

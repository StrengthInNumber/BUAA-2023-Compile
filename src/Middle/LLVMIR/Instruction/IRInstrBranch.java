package Middle.LLVMIR.Instruction;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrBranch;
import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
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

    public void generateAsm() {
        new AsmInstrMemory(AsmInstrOp.LW, Register.K0, Register.SP,
                AsmBuilder.getInstance().getOffsetOnStack(operands.get(0)));
        new AsmInstrBranch(AsmInstrOp.BEQ, Register.K0, Register.ZERO, operands.get(2).getName());
        new AsmInstrJump(AsmInstrOp.J, operands.get(1).getName());
    }
}

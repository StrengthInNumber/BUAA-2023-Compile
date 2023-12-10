package Middle.LLVMIR.Instruction;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrBranch;
import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;

public class IRInstrBranch extends IRInstr {
    public IRInstrBranch(IRValue cond, IRBasicBlock trueBB, IRBasicBlock falseBB, boolean autoInsert) {
        super(IRInstrType.BRANCH, IRIntegerType.INT1, autoInsert);
        addUseValue(cond);
        addUseValue(trueBB);
        addUseValue(falseBB);
    }

    public String toString() {
        return "br " + type + ' ' + useValues.get(0).getName()
                + ", label %" + useValues.get(1).getName()
                + ", label %" + useValues.get(2).getName();
    }

    public void generateAsm() {
        new AsmInstrMemory(AsmInstrOp.LW, Register.K0, Register.SP,
                AsmBuilder.getInstance().getOffsetOnStack(useValues.get(0)));
        new AsmInstrBranch(AsmInstrOp.BEQ, Register.K0, Register.ZERO, useValues.get(2).getName());
        new AsmInstrJump(AsmInstrOp.J, useValues.get(1).getName());
    }

    public IRBasicBlock getTrueBB() {
        return (IRBasicBlock) useValues.get(1);
    }

    public IRBasicBlock getFalseBB() {
        return (IRBasicBlock) useValues.get(2);
    }

    public void modifyTargetBB(IRBasicBlock oldBB, IRBasicBlock newBB) {
        useValues.set(useValues.indexOf(oldBB), newBB);
    }
}

package Middle.LLVMIR;

import Middle.LLVMIR.BasicBlock.IRBasicBlock;

public class IRBuilderLoop {
    private IRBasicBlock forStmt2BB;
    private IRBasicBlock afterForBB;

    public IRBuilderLoop(IRBasicBlock forStmt2BB, IRBasicBlock afterForBB) {
        this.forStmt2BB = forStmt2BB;
        this.afterForBB = afterForBB;
    }

    public IRBasicBlock getAfterForBB() {
        return afterForBB;
    }

    public IRBasicBlock getForStmt2BB() {
        return forStmt2BB;
    }
}

package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRUser;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRType;

public class IRInstr extends IRUser {
    protected IRInstrType instrType;
    private IRBasicBlock parentBB;

    public IRInstr(String name, IRInstrType instrType, IRType valueType, boolean autoInsert) {
        super(name, valueType);
        this.instrType = instrType;
        if(autoInsert) {
            IRBuilder.getInstance().addInstr(this);
        }
    }

    public IRInstr(IRInstrType instrType, IRType valueType, boolean autoInsert) {
        super(valueType);
        this.instrType = instrType;
        if(autoInsert) {
            IRBuilder.getInstance().addInstr(this);
        }
    }

    public IRInstrType getInstrType() {
        return instrType;
    }
    public void setParentBB(IRBasicBlock parentBB) {
        this.parentBB = parentBB;
    }

    public IRBasicBlock getParentBB() {
        return parentBB;
    }

    public void generateAsm() {
    }
}

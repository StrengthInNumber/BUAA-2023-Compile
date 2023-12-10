package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRUse;
import Middle.LLVMIR.IRUser;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;
import java.util.HashSet;

public class IRInstr extends IRUser {
    protected IRInstrType instrType;
    protected IRBasicBlock belongedBB;

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
    public void setBelongedBB(IRBasicBlock belongedBB) {
        this.belongedBB = belongedBB;
    }

    public IRBasicBlock getBelongedBB() {
        return belongedBB;
    }

    public void generateAsm() {
    }

    public void replaceWithNewValue(IRValue newValue) {
        ArrayList<IRUser> users = new ArrayList<>();
        for(IRUse use : uses) {
            users.add(use.getUser());
        }
        for(IRUser ur : users) {
            ur.replaceValue(this, newValue);
        }
    }

    public boolean isDeadCode() {
        if(instrType == IRInstrType.ALLOCA || this instanceof IRInstrAlu
                || instrType == IRInstrType.GEP || instrType == IRInstrType.LOAD
                || instrType == IRInstrType.PHI || instrType == IRInstrType.ZEXT) {
            return uses.isEmpty();
        }
        return false;
    }

    public void toRemove() {
        for(IRValue iv : useValues) {
            iv.removeUse(this);
        }
    }
}

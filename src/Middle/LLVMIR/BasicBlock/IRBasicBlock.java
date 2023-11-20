package Middle.LLVMIR.BasicBlock;

import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Type.IROtherType;

import java.util.ArrayList;

public class IRBasicBlock extends IRValue {
    private ArrayList<IRInstr> instructions;
    private IRFunction parentFunc;

    public IRBasicBlock(String name, boolean autoInsert) {
        super(name, IROtherType.BASIC_BLOCK);
        this.instructions = new ArrayList<>();
        this.parentFunc = null;
        if(autoInsert) {
            IRBuilder.getInstance().addBasicBlock(this);
        }
    }

    public void setParentFunc(IRFunction parentFunc) {
        this.parentFunc = parentFunc;
    }

    public void addInstr(IRInstr instr) {
        instructions.add(instr);
    }

    public IRInstr getLastInstr() {
        if(instructions.isEmpty()) {
            return null;
        } else {
            return instructions.get(instructions.size() - 1);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(":\n");
        for(IRInstr i : instructions) {
            sb.append(i).append('\n');
        }
        return sb.toString();
    }
}

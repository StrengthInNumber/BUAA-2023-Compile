package Middle.LLVMIR.Instruction.MemoryInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrAlu;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRUse;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrPhi;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRPointerType;
import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class IRInstrAlloca extends IRInstr {
    private IRType targetType;
    private ArrayList<IRInstr> instrUses = new ArrayList<>();
    private ArrayList<IRInstr> instrDefs = new ArrayList<>();
    private ArrayList<IRBasicBlock> bbUses = new ArrayList<>();
    private ArrayList<IRBasicBlock> bbDefs = new ArrayList<>();

    public IRInstrAlloca(String name, IRType targetType, boolean autoInsert) {
        super(name, IRInstrType.ALLOCA, new IRPointerType(targetType), autoInsert);
        this.targetType = targetType;
    }

    public String toString() {
        return name + " = alloca " + targetType;
    }

    public void generateAsm() {
        //先申请 targetType 对应的空间，然后看有没有为 name 的变量分配寄存器
        //如果分配了，就将申请空间对应的位置赋值给该寄存器
        //如果没有，就在栈上再申请一个空间存该地址
        int spaceAddr = AsmBuilder.getInstance().allocOnStack(targetType.getEleNum() * 4);
//        if(has allocated reg) {
//            //TODO:
//        }
        new AsmInstrAlu(AsmInstrOp.ADDI, Register.K0, Register.SP, spaceAddr);
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }

    public void mem2Reg(IRBasicBlock firstBB) {
        for (IRUse iu : uses) {
            if (!(iu.getUser() instanceof IRInstr)) {
                System.out.println("wrong in IRInstrAlloca.mem2Reg");
            } else {
                IRInstr instr = (IRInstr) iu.getUser();
                if (!instr.getBelongedBB().isDeleted()) {
                    if (instr.getInstrType() == IRInstrType.LOAD) {
                        bbUses.add(instr.getBelongedBB());
                        instrUses.add(instr);
                    } else if (instr.getInstrType() == IRInstrType.STORE) {
                        bbDefs.add(instr.getBelongedBB());
                        instrDefs.add(instr);
                    }
                }
            }
        }
        //Algorithm3.1
        HashSet<IRBasicBlock> F = new HashSet<>(); // set of basic blocks where phi is added
        ArrayList<IRBasicBlock> W = new ArrayList<>(bbDefs); // set of basic blocks that contain definitions of v
        while (!W.isEmpty()) {
            IRBasicBlock X = W.get(W.size() - 1);
            W.remove(X);
            for (IRBasicBlock Y : X.getDFList()) {
                if (!F.contains(Y)) {
                    IRInstrPhi phi = new IRInstrPhi(IRBuilder.getInstance().getLocalVarName(), Y);
                    instrDefs.add(phi);
                    instrUses.add(phi);
                    F.add(Y);
                    if (!bbDefs.contains(Y)) {
                        W.add(Y);
                    }
                }
            }
        }
        ArrayList<IRValue> valueStack = new ArrayList<>();
        valueStack.add(new IRConstInt(IRIntegerType.INT32, 0)); //加入初值 用于局部变量未定义的情况
        dfsForRename(firstBB, valueStack);
    }

    //Algorithm 3.3
    private void dfsForRename(IRBasicBlock nowBB, ArrayList<IRValue> valueStack) {
        int beforeSize = valueStack.size();
        Iterator<IRInstr> it = nowBB.getInstructions().iterator();
        while (it.hasNext()) {
            IRInstr instr = it.next();
            switch (instr.getInstrType()) {
                case PHI:
                    if(instrDefs.contains(instr)) {
                        valueStack.add(instr);
                    }
                    break;
                case STORE:
                    if(instrDefs.contains(instr)) {
                        valueStack.add(((IRInstrStore) instr).getSource());
                        it.remove();
                    }
                    break;
                case LOAD:
                    if(instrUses.contains(instr)) {
                        instr.replaceWithNewValue(valueStack.get(valueStack.size() - 1));
                        it.remove();
                    }
                    break;
                case ALLOCA:
                    if(instr.equals(this)) {
                        it.remove();
                    }
                    break;
            }
        }
        int afterSize = valueStack.size();
        //填充后继块的 phi 指令
        for (IRBasicBlock suc : nowBB.getCFGSuc()) {
            IRInstr sucBBPhi = suc.getInstructions().get(0);
            if (sucBBPhi.getInstrType() == IRInstrType.PHI && instrUses.contains(sucBBPhi)) {
                ((IRInstrPhi) sucBBPhi).addOp(valueStack.get(valueStack.size() - 1), nowBB);
            }
        }
        //dfs
        for (IRBasicBlock sucInDomTree : nowBB.getiDomList()) {
            dfsForRename(sucInDomTree, valueStack);
        }
        //将本节点压栈的所有 value 弹出
        ArrayList<IRValue> toDelete = new ArrayList<>();
        for (int i = beforeSize; i < afterSize; i++) {
            toDelete.add(valueStack.get(i));
        }
        for (IRValue iv : toDelete) {
            valueStack.remove(iv);
        }
    }
}

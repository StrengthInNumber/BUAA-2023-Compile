package Middle.LLVMIR.BasicBlock;

import Backend.InstrAsm.AsmInstrLabel;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.*;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrAlloca;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrMove;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IROtherType;
import Middle.LLVMIR.Type.IRPointerType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class IRBasicBlock extends IRValue {
    private ArrayList<IRInstr> instructions;
    private IRFunction parentFunc;
    private boolean isDeleted;
    private final ArrayList<IRBasicBlock> CFGPre = new ArrayList<>();
    private final ArrayList<IRBasicBlock> CFGSuc = new ArrayList<>();
    private final ArrayList<IRBasicBlock> domList = new ArrayList<>(); //该基本块支配的基本块
    private IRBasicBlock iDominator = null; //直接支配者（上级）
    private final ArrayList<IRBasicBlock> iDomList = new ArrayList<>(); //直接支配的块
    private final ArrayList<IRBasicBlock> DFList = new ArrayList<>(); //支配边界

    public IRBasicBlock(String name, boolean autoInsert) {
        super(name, IROtherType.BASIC_BLOCK);
        this.instructions = new ArrayList<>();
        this.parentFunc = null;
        this.isDeleted = false;
        if (autoInsert) {
            IRBuilder.getInstance().addBasicBlock(this);
        }
    }

    public void addBBToFunction(IRFunction f) {
        parentFunc = f;
        f.addBasicBlock(this);
    }
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setParentFunc(IRFunction parentFunc) {
        this.parentFunc = parentFunc;
    }

    public void addInstr(IRInstr instr) {
        instructions.add(instr);
    }

    public IRInstr getLastInstr() {
        if (instructions.isEmpty()) {
            return null;
        } else {
            return instructions.get(instructions.size() - 1);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(":\n");
        for (IRInstr i : instructions) {
            sb.append(i).append('\n');
        }
        return sb.toString();
    }

    public void generateAsm() {
        new AsmInstrLabel(name);
        for (IRInstr i : instructions) {
            i.generateAsm();
        }
    }

    public void deleteInstrAfter1stBr() {
        ArrayList<IRInstr> toRemove = new ArrayList<>();
        boolean had1stBr = false;
        for (IRInstr i : instructions) {
            if (had1stBr) {
                toRemove.add(i);
            }
            if (i.getInstrType() == IRInstrType.JUMP
                    || i.getInstrType() == IRInstrType.BRANCH
                    || i.getInstrType() == IRInstrType.RET) {
                had1stBr = true;
            }
        }
        instructions.removeAll(toRemove);
    }

    public void drawCFG() {
        IRInstr lastInstr = instructions.get(instructions.size() - 1);
        IRInstrType lastType = lastInstr.getInstrType();
        if (lastType == IRInstrType.BRANCH) {
            IRInstrBranch lb = (IRInstrBranch) lastInstr;
            CFGSuc.add(lb.getTrueBB());
            CFGSuc.add(lb.getFalseBB());
            lb.getTrueBB().CFGPre.add(this);
            lb.getFalseBB().CFGPre.add(this);
        } else if (lastType == IRInstrType.JUMP) {
            IRInstrJump lj = (IRInstrJump) lastInstr;
            CFGSuc.add(lj.getTargetBB());
            lj.getTargetBB().CFGPre.add(this);
        }
    }

    public ArrayList<IRBasicBlock> getCFGPre() {
        return CFGPre;
    }

    public ArrayList<IRBasicBlock> getCFGSuc() {
        return CFGSuc;
    }

    public void getDom(ArrayList<IRBasicBlock> blocksInFunction) {
        HashSet<IRBasicBlock> notDom = new HashSet<>();
        this.dfsGetDom(blocksInFunction.get(0), notDom);
        for (IRBasicBlock bb : blocksInFunction) {
            if (!notDom.contains(bb)) {
                domList.add(bb);
            }
        }
    }

    // 找到所有不被本块支配的BB
    // 从入口走 dfs，当碰到 target 的时候就 return，这样就保证了所有只能
    // 通过 target 到达的点都不被遍历到，故在 reached 中的都不被 target 支配
    public void dfsGetDom(IRBasicBlock firstBB, HashSet<IRBasicBlock> notDom) {
        if (!firstBB.equals(this)) {
            notDom.add(firstBB);
            for (IRBasicBlock sucBBOfFirst : firstBB.CFGSuc) {
                if (!notDom.contains(sucBBOfFirst)) {
                    this.dfsGetDom(sucBBOfFirst, notDom);
                }
            }
        }
    }

    public void getIDom() {
        for (IRBasicBlock domedBB : domList) {
            if (isImmediateDominate(domedBB)) {
                domedBB.iDominator = this;
                iDomList.add(domedBB);
            }
        }
    }

    private boolean isStrictlyDominate(IRBasicBlock domedBB) {
        return !domedBB.equals(this) && this.domList.contains(domedBB);
    }

    //判断 this 是否直接支配 domedBB
    private boolean isImmediateDominate(IRBasicBlock domedBB) {
        if (!this.isStrictlyDominate(domedBB)) { //首先判定严格支配
            return false;
        } else {
            for (IRBasicBlock closerDominator : domList) {
                if (!(closerDominator.equals(this) || closerDominator.equals(domedBB))
                        && closerDominator.domList.contains(domedBB)) {
                    //如果 this 严格支配 cd，cd 严格支配 domedBB，那么 this 就不是 domedBB 的直接支配者
                    return false;
                }
            }
            return true;
        }
    }

    public void drawDF() { //遍历当前基本块的所有出边，画 DF
       for(IRBasicBlock b : CFGSuc) {
           IRBasicBlock x = this;
           while(!x.isStrictlyDominate(b)) {
               x.DFList.add(b);
               x = x.iDominator;
               if(x == null) {
                   break;
               }
           }
       }
    }

    public void mem2Reg(IRBasicBlock firstBB) {
        ArrayList<IRInstrAlloca> allocas = new ArrayList<>();
        for(IRInstr instr : instructions) {
            if(instr.getInstrType() == IRInstrType.ALLOCA
                    && instr.getType() instanceof IRPointerType
                    && ((IRPointerType)instr.getType()).getContentType() == IRIntegerType.INT32){
                //选择 alloca 指令
                allocas.add((IRInstrAlloca) instr);
            }
        }
        for(IRInstrAlloca alloca : allocas) {
            alloca.mem2Reg(firstBB);
        }
    }

    public void printDF() {
        System.out.println(this.name + "'s DF is ");
        for(IRBasicBlock bb : DFList) {
            System.out.println(bb.name);
        }
        System.out.print('\n');
    }

    public ArrayList<IRBasicBlock> getDFList() {
        return DFList;
    }

    public void addPhi(IRInstr phi) {
        instructions.add(0, phi);
    }

    public ArrayList<IRInstr> getInstructions() {
        return instructions;
    }

    public IRInstr getFirstInstr() {
        return instructions.get(0);
    }

    public ArrayList<IRBasicBlock> getiDomList() {
        return iDomList;
    }

    public void deleteDeadCodeFromBegin() {
        Iterator<IRInstr> it = instructions.iterator();
        while(it.hasNext()){
            IRInstr instr = it.next();
            if(instr.isDeadCode()) {
                instr.toRemove();
                it.remove();
            }
        }
    }
    public void deleteDeadCodeFromEnd() {
        ArrayList<IRInstr> deadCodes = new ArrayList<>();
        for(int i = instructions.size() - 1; i >= 0; i--) {
            IRInstr instr = instructions.get(i);
            if(instructions.get(i).isDeadCode()) {
                instr.toRemove();
                deadCodes.add(instr);
            }
        }
        for(IRInstr i : deadCodes) {
            instructions.remove(i);
        }
    }

    public void modifyBranch(IRBasicBlock oldBB, IRBasicBlock newBB) {
        IRInstr lastInstr = instructions.get(instructions.size() - 1);
        if(lastInstr.getInstrType() != IRInstrType.BRANCH) {
            System.out.println("wrong in IRBasicBlock.modifyBranch");
        } else {
            ((IRInstrBranch) lastInstr).modifyTargetBB(oldBB, newBB);
        }
    }

    public void phiToPCopy() {
        ArrayList<IRBasicBlock> preBBs = new ArrayList<>(CFGPre);
        ArrayList<IRInstrPCopy> pCopies = new ArrayList<>();
        for (IRBasicBlock preBB : preBBs) {
            IRInstrPCopy pCopy = new IRInstrPCopy(IRBuilder.getInstance().getLocalVarName());
            pCopies.add(pCopy);
            if (preBB.CFGSuc.size() == 1) { //直接将 pCopy 插到末尾跳转语句前
                preBB.instructions.add(preBB.instructions.size() - 1, pCopy);
                pCopy.setBelongedBB(preBB);
            } else {
                IRBasicBlock midBB = new IRBasicBlock(
                        IRBuilder.getInstance().getBasicBlockName(), false);
                midBB.parentFunc = this.parentFunc;
                parentFunc.addBasicBlock(midBB);
                midBB.instructions.add(pCopy);
                pCopy.setBelongedBB(midBB);
                preBB.modifyBranch(this, midBB); //因为有多个后继，所以最后一条指令一定是 br
                IRInstrJump j = new IRInstrJump(this, false);
                j.setBelongedBB(midBB);
                midBB.instructions.add(j);
                preBB.CFGSuc.set(preBB.CFGSuc.indexOf(this), midBB);
                this.CFGPre.set(this.CFGPre.indexOf(preBB), midBB);
                midBB.CFGPre.add(preBB);
                midBB.CFGSuc.add(this);
            }
        }
        ArrayList<IRInstrPhi> phis = new ArrayList<>();
        for (IRInstr i : instructions) {
            if(i.getInstrType() == IRInstrType.PHI) {
                phis.add((IRInstrPhi) i);
            }
        }
        for(IRInstrPhi phi : phis) {
            phi.insertOptionToPCopy(pCopies);
            instructions.remove(phi);
        }
    }

    public void pCopyToMove() {
        ArrayList<IRInstrPCopy> pCopies = new ArrayList<>();
        for(IRInstr instr : instructions) {
            if(instr.getInstrType() == IRInstrType.PCOPY) {
                pCopies.add((IRInstrPCopy) instr);
            }
        }
        for(IRInstrPCopy pCopy : pCopies) {
            instructions.remove(pCopy);
            for(IRInstrMove move : pCopy.toMoveInstr()) {
                instructions.add(instructions.size() - 1, move);
            }
        }
    }
}

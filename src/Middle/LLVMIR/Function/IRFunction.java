package Middle.LLVMIR.Function;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrLabel;
import Backend.Register;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.*;
import Middle.LLVMIR.Instruction.FunctionInstr.IRInstrRet;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IROtherType;
import Middle.LLVMIR.Type.IRType;
import Middle.LLVMIR.Type.IRVoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class IRFunction extends IRValue {
    private final ArrayList<IRParam> params;
    private final ArrayList<IRBasicBlock> blocks;
    private final IRType returnType;
    private final HashMap<IRBasicBlock, ArrayList<IRBasicBlock>> CFGPre = new HashMap<>();
    private final HashMap<IRBasicBlock, ArrayList<IRBasicBlock>> CFGSuc = new HashMap<>();
    private final HashMap<IRBasicBlock, IRBasicBlock> dominantTreeParent = new HashMap<>();
    private final HashMap<IRBasicBlock, ArrayList<IRBasicBlock>> dominantTreeChild = new HashMap<>();
    private final HashMap<IRValue, Register> varToReg = new HashMap<>();

    public IRFunction(String name, IRType returnType, ArrayList<IRParam> params){
        super(name, IROtherType.FUNCTION);
        this.params = params;
        blocks = new ArrayList<>();
        IRBuilder.getInstance().addFunction(this);
        this.returnType = returnType;
    }

    public void addBasicBlock(IRBasicBlock bb) {
        this.blocks.add(bb);
    }

    public IRType getReturnType() {
        return returnType;
    }

    public void checkReturn() {
        if(blocks.get(blocks.size() - 1).getLastInstr() == null ||
                blocks.get(blocks.size() - 1).getLastInstr().getInstrType() != IRInstrType.RET){
            if(returnType == IRIntegerType.INT32) {
                blocks.get(blocks.size() - 1).addInstr(
                        new IRInstrRet(new IRConstInt(IRIntegerType.INT32, 0), false));
            } else {
                blocks.get(blocks.size() - 1).addInstr(
                        new IRInstrRet(null, false));
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ").append(returnType).append(' ').append(name).append('(');
        if(!params.isEmpty()) {
            sb.append(params.get(0));
        }
        for(int i = 1; i < params.size(); i++) {
            sb.append(',').append(params.get(i));
        }
        sb.append(") {\n");
        for(IRBasicBlock bb : blocks) {
            sb.append(bb);
        }
        sb.append("}\n\n");
        return sb.toString();
    }

    public void generateAsm() {
        new AsmInstrLabel(name.substring(1));
        AsmBuilder.getInstance().prepareForFunc();
        for(IRParam param : params) {
            AsmBuilder.getInstance().pushToStack(param, 4);
        }
        for(IRBasicBlock bb : blocks) {
            bb.generateAsm();
        }
    }

    public void deleteInstrAfter1stBr() {
        for(IRBasicBlock bb : blocks) {
            bb.deleteInstrAfter1stBr();
        }
    }

    public void drawCFG() {
        for(IRBasicBlock bb : blocks) {
            bb.drawCFG();
        }
    }

    public void deleteUnreachableBB() {
        HashSet<IRBasicBlock> reachedBB = new HashSet<>();
        reachedBB.add(blocks.get(0));
        dfsReachBB(blocks.get(0), reachedBB);
        Iterator<IRBasicBlock> it = blocks.iterator();
        while (it.hasNext()) {
            IRBasicBlock bb = it.next();
            if (! reachedBB.contains(bb)) {
                it.remove();
                bb.setDeleted(true); // 为基本块打上“被删除”的标记
            }
        }
    }

    public void dfsReachBB(IRBasicBlock nowBB, HashSet<IRBasicBlock> reachedBB) {
        reachedBB.add(nowBB);
        for(IRBasicBlock bb : nowBB.getCFGSuc()) {
            if(!reachedBB.contains(bb)) {
                dfsReachBB(bb, reachedBB);
            }
        }
    }
    //求所有的基本块所支配的块
    public void getDom() {
        for(IRBasicBlock bb : blocks) {
            bb.getDom(blocks);
        }
    }

    public void getIDom() {
        for(IRBasicBlock bb : blocks) {
            bb.getIDom();
        }
    }

    public void drawDF() {
        for(IRBasicBlock bb : blocks) {
            bb.drawDF();
        }
    }

    public void mem2Reg() {
        for(IRBasicBlock bb : blocks) {
            bb.mem2Reg(blocks.get(0));
        }
    }

    public void printDF() {
        for(IRBasicBlock bb : blocks) {
            bb.printDF();
        }
    }

    public void deleteDeadCode() {
        deleteDeadCodeFromEnd();
        deleteDeadCodeFromBegin();
        deleteDeadCodeFromEnd();
    }

    public void deleteDeadCodeFromBegin() {
        for(IRBasicBlock bb : blocks) {
            bb.deleteDeadCodeFromBegin();
        }
    }
    public void deleteDeadCodeFromEnd() {
        for(int i = blocks.size() - 1; i >= 0; i--) {
            blocks.get(i).deleteDeadCodeFromEnd();
        }
    }

    public void removePhi() {
        phiToPCopy();
        pCopyToMove();
    }

    private void phiToPCopy() {
        ArrayList<IRBasicBlock> toRemovePhiBB = new ArrayList<>();
        for(IRBasicBlock bb : blocks) { //找到所有含有 phi 指令的 bb
            if(bb.getFirstInstr().getInstrType() == IRInstrType.PHI) {
                toRemovePhiBB.add(bb);
            }
        }
        for(IRBasicBlock bb : toRemovePhiBB) {
            bb.phiToPCopy();
        }
    }

    private void pCopyToMove() {
        for(IRBasicBlock bb : blocks) {
            bb.pCopyToMove();
        }
    }
}

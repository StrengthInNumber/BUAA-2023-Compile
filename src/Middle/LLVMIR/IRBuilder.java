package Middle.LLVMIR;

import Middle.IRBuilderLoop;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.GlobalVar.IRGlobalVar;
import Middle.LLVMIR.Instruction.FunctionInstr.IRInstrRet;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;

public class IRBuilder {
    private static final IRBuilder IR_BUILDER = new IRBuilder();
    private int globalVarCount = 0;
    private int fParamCount = 0;
    private int basicBlockCount = 0;
    private int localVarCount = 0;
    private int irStringCount = 0;
    public static IRBuilder getInstance() {
        return IR_BUILDER;
    }
    private IRModule curModule;
    private IRFunction curFunction;
    private IRBasicBlock curBasicBlock;

    private ArrayList<IRBuilderLoop> loopStack = new ArrayList<>();
    public String getGlobalVarName() {
        int r = globalVarCount;
        globalVarCount++;
        return "@Global_" + r;
    }

    public String getFParamName() {
        int r = fParamCount;
        fParamCount++;
        return "%FParam_" + r;
    }

    public String getBasicBlockName() {
        int r = basicBlockCount;
        basicBlockCount++;
        return "BB_" + r;
    }

    public String getLocalVarName() {
        int r = localVarCount;
        localVarCount++;
        return "%Local_" + r;
    }

    public String getIRStringName() {
        int r = irStringCount;
        irStringCount++;
        return "str" + r;
    }

    public String getFunctionName(String name) {
        return "@f_" + name;
    }

    public void setCurFunction(IRFunction curFunction) {
        this.curFunction = curFunction;
    }

    public void setCurModule(IRModule curModule) {
        this.curModule = curModule;
    }

    public void setCurBasicBlock(IRBasicBlock curBasicBlock) {
        this.curBasicBlock = curBasicBlock;
    }

    public void addInstr(IRInstr i) {
        curBasicBlock.addInstr(i);
        i.setParentBB(curBasicBlock);
    }

    public void addGlobalVar(IRGlobalVar gv) {
        curModule.addGlobalVar(gv);
    }

    public void addFunction(IRFunction f) {
        curModule.addFunction(f);
    }

    public void addIRString(IRString s) {
        curModule.addIRString(s);
    }

    public void addBasicBlock(IRBasicBlock bb) {
        curFunction.addBasicBlock(bb);
        bb.setParentFunc(curFunction);
    }
    public IRModule getCurModule() {
        return curModule;
    }

    public void enterLoop(IRBasicBlock curCircle_forStmt2BB, IRBasicBlock curCircle_afterForBB) {
        loopStack.add(new IRBuilderLoop(curCircle_forStmt2BB, curCircle_afterForBB));
    }

    public IRBasicBlock getCurCircle_afterForBB() {
        return loopStack.get(loopStack.size() - 1).getAfterForBB();
    }

    public IRBasicBlock getCurCircle_forStmt2BB() {
        return loopStack.get(loopStack.size() - 1).getForStmt2BB();
    }

    public void leaveLoop() {
        loopStack.remove(loopStack.size() - 1);
    }

    public void addReturn(IRType returnType) {
        if(curBasicBlock.getLastInstr() == null || curBasicBlock.getLastInstr().getInstrType() != IRInstrType.RET) {
            if (returnType == IRIntegerType.INT32) {
                curBasicBlock.addInstr(
                        new IRInstrRet(new IRConstInt(IRIntegerType.INT32, 0), false));
            } else {
                curBasicBlock.addInstr(
                        new IRInstrRet(null, false));
            }
        }
    }
}

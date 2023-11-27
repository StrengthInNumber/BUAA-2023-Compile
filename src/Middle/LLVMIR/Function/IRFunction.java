package Middle.LLVMIR.Function;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrLabel;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.FunctionInstr.IRInstrRet;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IROtherType;
import Middle.LLVMIR.Type.IRType;
import Middle.LLVMIR.Type.IRVoidType;

import java.util.ArrayList;

public class IRFunction extends IRValue {
    private ArrayList<IRParam> params;
    private ArrayList<IRBasicBlock> blocks;
    private IRType returnType;

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
}

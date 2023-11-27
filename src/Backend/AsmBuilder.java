package Backend;

import Backend.InstrAsm.*;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.GlobalVar.IRGlobalVar;
import Middle.LLVMIR.IRValue;

import java.util.ArrayList;
import java.util.HashMap;

public class AsmBuilder {
    private static AsmBuilder asmBuilder = new AsmBuilder();
    public static AsmBuilder getInstance() {return asmBuilder;}
    private ArrayList<AsmInstr> dataInstructions = new ArrayList<>();
    private ArrayList<AsmInstr> textInstructions = new ArrayList<>();
    private int stackOffset = 0;
    private HashMap<IRValue, Integer> stackMap;

    public void addDataInstr(AsmInstr ai) {
        dataInstructions.add(ai);
    }

    public void addTextInstr(AsmInstr ai) {
        textInstructions.add(ai);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");
        for(AsmInstr ai : dataInstructions){
            sb.append(ai).append('\n');
        }
        sb.append(".text\n");
        for(AsmInstr ai : textInstructions){
            sb.append(ai).append('\n');
        }
        return sb.toString();
    }

    public void prepareForFunc() { //栈帧初始化
        stackMap = new HashMap<>();
        stackOffset = 0;
    }

    public int pushToStack(IRValue value, int size) {
        stackOffset -= size;
        stackMap.put(value, stackOffset);
        return stackOffset;
    }

    public void pushToStackAt(IRValue value, int location) {
        stackMap.put(value, location);
    }

    public int allocOnStack(int size) {
        stackOffset -= size;
        return stackOffset;
    }

    public Integer getOffsetOnStack(IRValue iv) {
        return stackMap.get(iv);
    }

    public void asmGetOperand(IRValue op, Register r) {
        Integer offset = getOffsetOnStack(op);
        if(op instanceof IRConstInt) {
            new AsmInstrLi(r, ((IRConstInt) op).getVal());
            //TODO: 如果分配寄存器了
            //未分配寄存器
        } else {
            if(offset == null) {
                offset = pushToStack(op, 4);
                new AsmComment("curOffset=" + stackOffset);
            }
            new AsmInstrMemory(AsmInstrOp.LW, r, Register.SP, offset);
        }
    }

    public void asmGetPointer(IRValue pointer, Register r) {
        if (pointer instanceof IRGlobalVar) {
            new AsmInstrLa(r, pointer.getName().substring(1));
        } else {
            Integer offset = getOffsetOnStack(pointer);
            if (offset == null) {
                offset = pushToStack(pointer, 4);
                new AsmComment("curOffset=" + stackOffset);
            }
            new AsmInstrMemory(AsmInstrOp.LW, r, Register.SP, offset);
        }
    }

    public int getStackOffset() {
        return stackOffset;
    }
}

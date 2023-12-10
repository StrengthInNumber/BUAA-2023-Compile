package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.Constant.IRConst;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrMove;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrStore;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRVoidType;

import java.util.ArrayList;
import java.util.HashSet;

public class IRInstrPCopy extends IRInstr {
    private ArrayList<IRInstrPhi> phis = new ArrayList<>();
    private ArrayList<IRValue> options = new ArrayList<>();
    public IRInstrPCopy(String name) {
        super(name, IRInstrType.PCOPY, IRVoidType.VOID, false);
    }

    public void addCopy(IRInstrPhi phi, IRValue opt) {
        phis.add(phi);
        options.add(opt);
    }

    public ArrayList<IRInstrMove> toMoveInstr() {
        ArrayList<IRInstrMove> moves = new ArrayList<>();
        for(int i = 0; i < phis.size(); i++) { //将所有 pCopy 指令变为 move
            moves.add(new IRInstrMove(phis.get(i), options.get(i), false));
        }
        ArrayList<IRInstrMove> temps = new ArrayList<>(); //记录增加的临时变量的 move
        HashSet<IRValue> finished = new HashSet<>();
        for(int i = 0; i < moves.size(); i++) {
            IRValue toValue = moves.get(i).getToValue();
            if(finished.contains(toValue) || toValue instanceof IRConst) {
                continue;
            }
            for(int j = i + 1; j < moves.size(); j++) {
                if(moves.get(j).getFromValue().equals(toValue)){ //发现循环赋值
                    IRValue tempValue = new IRValue(toValue.getName() + "Temp", IRIntegerType.INT32);
                    for (IRInstrMove move : moves) {
                        if (move.getFromValue().equals(toValue)) {
                            move.setFromValue(tempValue);
                        }
                    }
                    temps.add(new IRInstrMove(tempValue, toValue, false));
                }
            }
            finished.add(toValue);
        }
        for(IRInstrMove temp : temps) { //将所有的临时变量的 move 加到 moves 的最前面
            moves.add(0, temp);
        }
        for(IRInstrMove move : moves) {
            move.setBelongedBB(this.belongedBB);
        }
        return moves;
    }

    public String toString() {
        return "pcopy " + phis.size();
    }
}

package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;

import java.util.ArrayList;

public class IRInstrPhi extends IRInstr {
    private ArrayList<IRBasicBlock> preBBs;

    public IRInstrPhi(String name, IRBasicBlock parentBB) {
        super(name, IRInstrType.PHI, IRIntegerType.INT32, false);
        preBBs = parentBB.getCFGPre();
        for(IRBasicBlock bb : preBBs) {
            useValues.add(null); //占位
        }
        this.belongedBB = parentBB;
        parentBB.addPhi(this);
    }

    public void addOp(IRValue iv, IRBasicBlock preBB) {
        useValues.set(preBBs.indexOf(preBB), iv);
        iv.addUse(this);
    }

    public void insertOptionToPCopy(ArrayList<IRInstrPCopy> pCopies) {
        for(int i = 0; i < useValues.size(); i++) {
                pCopies.get(i).addCopy(this, useValues.get(i));
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = phi ").append(type).append(' ');
        for(int i = 0; i < preBBs.size(); i++) {
            sb.append("[ ");
            sb.append(useValues.get(i).getName());
            sb.append(", %");
            sb.append(preBBs.get(i).getName());
            if(i == preBBs.size() - 1) {
                sb.append(" ]");
            } else {
                sb.append(" ], ");
            }
        }
        return sb.toString();
    }
}

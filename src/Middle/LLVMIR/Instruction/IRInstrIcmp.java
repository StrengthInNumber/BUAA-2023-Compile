package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;

public class IRInstrIcmp extends IRInstr {
    public IRInstrIcmp(String name, IRInstrType instrType, boolean autoInsert, IRValue op1, IRValue op2) {
        super(name, instrType, IRIntegerType.INT1, autoInsert);
        operands.add(op1);
        operands.add(op2);
    }

    public String toString() {
        return name + " = " + "icmp " + instrType.toString().toLowerCase()
                + " i32 "
                + operands.get(0).getName()
                + ", " + operands.get(1).getName();
    }
}

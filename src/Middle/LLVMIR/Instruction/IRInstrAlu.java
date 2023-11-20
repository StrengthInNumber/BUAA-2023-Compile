package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRType;

public class IRInstrAlu extends IRInstr {
    public IRInstrAlu(String name, IRInstrType instrType, boolean autoInsert, IRValue op1, IRValue op2) {
        super(name, instrType, IRIntegerType.INT32, autoInsert);
        operands.add(op1);
        operands.add(op2);
    }

    public String toString() {
        return name + " = " + instrType.toString().toLowerCase()
                + " i32 "
                + operands.get(0).getName()
                + ", " + operands.get(1).getName();
    }
}

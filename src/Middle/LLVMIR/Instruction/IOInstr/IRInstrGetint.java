package Middle.LLVMIR.Instruction.IOInstr;

import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRIntegerType;

public class IRInstrGetint extends IRInstr {
    public IRInstrGetint(String name) {
        super(name, IRInstrType.GETINT, IRIntegerType.INT32, true);
    }

    public String toString() {
        return name + " = call i32 @getint()";
    }
}

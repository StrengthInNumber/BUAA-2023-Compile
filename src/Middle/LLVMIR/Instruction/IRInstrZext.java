package Middle.LLVMIR.Instruction;

import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRType;

public class IRInstrZext extends IRInstr {
    public IRType targetType;
    public IRInstrZext(String name, boolean autoInsert,
                       IRValue source, IRType targetType) {
        super(name, IRInstrType.ZEXT, targetType, autoInsert);
        this.targetType = targetType;
        operands.add(source);
    }

    public String toString() {
        return name + " = zext "
                + operands.get(0).getType() + " "
                + operands.get(0).getName() + " to "
                + targetType;
    }
}

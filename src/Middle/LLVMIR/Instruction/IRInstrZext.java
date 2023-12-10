package Middle.LLVMIR.Instruction;

import Backend.AsmBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRType;

public class IRInstrZext extends IRInstr {
    public IRType targetType;
    public IRInstrZext(String name, boolean autoInsert,
                       IRValue source, IRType targetType) {
        super(name, IRInstrType.ZEXT, targetType, autoInsert);
        this.targetType = targetType;
        addUseValue(source);
    }

    public String toString() {
        return name + " = zext "
                + useValues.get(0).getType() + " "
                + useValues.get(0).getName() + " to "
                + targetType;
    }

    public void generateAsm() {
        AsmBuilder.getInstance().pushToStackAt(this,
                AsmBuilder.getInstance().getOffsetOnStack(useValues.get(0)));
    }
}

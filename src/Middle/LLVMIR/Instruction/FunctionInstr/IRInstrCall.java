package Middle.LLVMIR.Instruction.FunctionInstr;

import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

import java.util.ArrayList;

public class IRInstrCall extends IRInstr {
    public IRInstrCall(String name, IRFunction function, ArrayList<IRValue> fRParams, boolean autoInsert) {
        super(name, IRInstrType.CALL, function.getReturnType(), autoInsert);
        operands.add(function);
        operands.addAll(fRParams);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        IRFunction f = (IRFunction) operands.get(0);
        if(type == IRVoidType.VOID) {
            sb.append("call void ");
        } else {
            sb.append(name).append(" = call i32 ");
        }
        sb.append(f.getName()).append('(');
        if(operands.size() > 1) {
            sb.append(operands.get(1).getType()).append(" ").append(operands.get(1).getName());
            for (int i = 2; i < operands.size(); i++) {
                sb.append(", ").append(operands.get(i).getType()).append(" ").append(operands.get(i).getName());
            }
        }
        sb.append(')');
        return sb.toString();
    }
}

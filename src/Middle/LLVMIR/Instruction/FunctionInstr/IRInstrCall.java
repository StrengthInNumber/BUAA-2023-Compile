package Middle.LLVMIR.Instruction.FunctionInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrAlu;
import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.Function.IRParam;
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

    @Override
    public void generateAsm() {
        //TODO: save register
        //save sp ra to stack
        new AsmInstrMemory(AsmInstrOp.SW, Register.SP, Register.SP,
                AsmBuilder.getInstance().allocOnStack(4));
        new AsmInstrMemory(AsmInstrOp.SW, Register.RA, Register.SP,
                AsmBuilder.getInstance().allocOnStack(4));
        //传参
        //TODO: pass the param to register
        for(int i = 1; i < operands.size(); i++) {
            AsmBuilder.getInstance().asmGetOperand(operands.get(i), Register.K0);
            new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                    AsmBuilder.getInstance().allocOnStack(4));
        }
        //设置被调函数栈底为压完 sp 和 ra 之后的栈指针位置
        new AsmInstrAlu(AsmInstrOp.ADDI, Register.SP, Register.SP,
                AsmBuilder.getInstance().getStackOffset() + (operands.size() - 1) * 4);
        new AsmInstrJump(AsmInstrOp.JAL, operands.get(0).getName().substring(1));
        //restore sp ra
        new AsmInstrMemory(AsmInstrOp.LW, Register.RA, Register.SP, 0);
        new AsmInstrMemory(AsmInstrOp.LW, Register.SP, Register.SP, 4);
        //TODO: restore registers
        //获取返回值：可以不用判断是否需要返回值 都先存 V0寄存器值
        new AsmInstrMemory(AsmInstrOp.SW, Register.V0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

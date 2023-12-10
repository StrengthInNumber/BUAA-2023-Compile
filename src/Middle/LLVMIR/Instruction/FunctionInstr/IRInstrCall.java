package Middle.LLVMIR.Instruction.FunctionInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.*;
import Backend.Register;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRVoidType;

import java.util.ArrayList;

public class IRInstrCall extends IRInstr {
    public IRInstrCall(String name, IRFunction function, ArrayList<IRValue> fRParams, boolean autoInsert) {
        super(name, IRInstrType.CALL, function.getReturnType(), autoInsert);
        addUseValue(function);
        for(IRValue value : fRParams) {
            addUseValue(value);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        IRFunction f = (IRFunction) useValues.get(0);
        if(type == IRVoidType.VOID) {
            sb.append("call void ");
        } else {
            sb.append(name).append(" = call i32 ");
        }
        sb.append(f.getName()).append('(');
        if(useValues.size() > 1) {
            sb.append(useValues.get(1).getType()).append(" ").append(useValues.get(1).getName());
            for (int i = 2; i < useValues.size(); i++) {
                sb.append(", ").append(useValues.get(i).getType()).append(" ").append(useValues.get(i).getName());
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
        for(int i = 1; i < useValues.size(); i++) {
            if(useValues.get(i) instanceof IRConstInt) {
                new AsmInstrLi(Register.K0, ((IRConstInt) useValues.get(i)).getVal());
                new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                        AsmBuilder.getInstance().allocOnStack(4));
            } else {
                Integer offset = AsmBuilder.getInstance().getOffsetOnStack(useValues.get(i));
                if (offset == null) {
                    AsmBuilder.getInstance().pushToStack(useValues.get(i), 4);
                } else {
                    AsmBuilder.getInstance().asmGetOperand(useValues.get(i), Register.K0);
                    new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                            AsmBuilder.getInstance().allocOnStack(4));
                }
            }
        }
        //设置被调函数栈底为压完 sp 和 ra 之后的栈指针位置
        new AsmInstrAlu(AsmInstrOp.ADDI, Register.SP, Register.SP,
                AsmBuilder.getInstance().getStackOffset() + (useValues.size() - 1) * 4);
        new AsmInstrJump(AsmInstrOp.JAL, useValues.get(0).getName().substring(1));
        //restore sp ra
        new AsmInstrMemory(AsmInstrOp.LW, Register.RA, Register.SP, 0);
        new AsmInstrMemory(AsmInstrOp.LW, Register.SP, Register.SP, 4);
        //TODO: restore registers
        //获取返回值：可以不用判断是否需要返回值 都先存 V0寄存器值
        new AsmInstrMemory(AsmInstrOp.SW, Register.V0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

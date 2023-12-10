package Middle.LLVMIR.Instruction;

import Backend.AsmBuilder;
import Backend.InstrAsm.*;
import Backend.Register;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;

public class IRInstrIcmp extends IRInstr {
    public IRInstrIcmp(String name, IRInstrType instrType, boolean autoInsert, IRValue op1, IRValue op2) {
        super(name, instrType, IRIntegerType.INT1, autoInsert);
        addUseValue(op1);
        addUseValue(op2);
    }

    public String toString() {
        return name + " = " + "icmp " + instrType.toString().toLowerCase()
                + " i32 "
                + useValues.get(0).getName()
                + ", " + useValues.get(1).getName();
    }

    public void generateAsm() {
        AsmBuilder.getInstance().asmGetOperand(useValues.get(0), Register.K0);
        AsmBuilder.getInstance().asmGetOperand(useValues.get(1), Register.K1);
        switch (instrType) {
            case EQ:
                new AsmInstrCmp(AsmInstrOp.SEQ, Register.K0, Register.K0, Register.K1);
                break;
            case NE:
                new AsmInstrCmp(AsmInstrOp.SNE, Register.K0, Register.K0, Register.K1);
                break;
            case SGT:
                new AsmInstrCmp(AsmInstrOp.SGT, Register.K0, Register.K0, Register.K1);
                break;
            case SGE:
                new AsmInstrCmp(AsmInstrOp.SGE, Register.K0, Register.K0, Register.K1);
                break;
            case SLT:
                new AsmInstrCmp(AsmInstrOp.SLT, Register.K0, Register.K0, Register.K1);
                break;
            case SLE:
                new AsmInstrCmp(AsmInstrOp.SLE, Register.K0, Register.K0, Register.K1);
                break;
            default:
                System.out.println("wrong in IRInstrIcmp.generateAsm: InstrType is " + instrType);
                break;
        }
        //TODO:若 name 分配了寄存器，则对其寄存器赋值
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

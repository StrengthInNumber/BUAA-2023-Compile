package Middle.LLVMIR.Instruction;

import Backend.AsmBuilder;
import Backend.InstrAsm.*;
import Backend.Register;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;

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

    public void generateAsm() {
        AsmBuilder.getInstance().asmGetOperand(operands.get(0), Register.K0);
        AsmBuilder.getInstance().asmGetOperand(operands.get(1), Register.K1);
        switch (instrType) {
            case ADD:
                new AsmInstrAlu(AsmInstrOp.ADDU, Register.K0, Register.K0, Register.K1);
                break;
            case SUB:
                new AsmInstrAlu(AsmInstrOp.SUBU, Register.K0, Register.K0, Register.K1);
                break;
            case MUL:
                new AsmInstrAlu(AsmInstrOp.MULT, Register.K0, Register.K1);
                new AsmInstrHiLo(AsmInstrOp.MFLO, Register.K0);
                break;
            case SDIV:
                new AsmInstrAlu(AsmInstrOp.DIV, Register.K0, Register.K1);
                new AsmInstrHiLo(AsmInstrOp.MFLO, Register.K0);
                break;
            case SREM:
                new AsmInstrAlu(AsmInstrOp.DIV, Register.K0, Register.K1);
                new AsmInstrHiLo(AsmInstrOp.MFHI, Register.K0);
                break;
            default:
                System.out.println("wrong in IRInstrAlu.generateAsm: InstrType is " + instrType);
                break;
        }
        //TODO:若 name 分配了寄存器，则对其寄存器赋值
        int offset = AsmBuilder.getInstance().pushToStack(this, 4);
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP, offset);
    }
}

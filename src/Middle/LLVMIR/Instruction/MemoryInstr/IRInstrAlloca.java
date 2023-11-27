package Middle.LLVMIR.Instruction.MemoryInstr;

import Backend.AsmBuilder;
import Backend.InstrAsm.AsmInstrAlu;
import Backend.InstrAsm.AsmInstrMemory;
import Backend.InstrAsm.AsmInstrOp;
import Backend.Register;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Type.IRPointerType;
import Middle.LLVMIR.Type.IRType;

public class IRInstrAlloca extends IRInstr {
    private IRType targetType;

    public IRInstrAlloca(String name, IRType targetType, boolean autoInsert) {
        super(name, IRInstrType.ALLOCA, new IRPointerType(targetType), autoInsert);
        this.targetType = targetType;
    }

    public String toString() {
        return name + " = alloca " + targetType;
    }

    public void generateAsm() {
        //先申请 targetType 对应的空间，然后看有没有为 name 的变量分配寄存器
        //如果分配了，就将申请空间对应的位置赋值给该寄存器
        //如果没有，就在栈上再申请一个空间存该地址
        int spaceAddr = AsmBuilder.getInstance().allocOnStack(targetType.getEleNum() * 4);
//        if(has allocated reg) {
//            //TODO:
//        }
        new AsmInstrAlu(AsmInstrOp.ADDI, Register.K0, Register.SP, spaceAddr);
        new AsmInstrMemory(AsmInstrOp.SW, Register.K0, Register.SP,
                AsmBuilder.getInstance().pushToStack(this, 4));
    }
}

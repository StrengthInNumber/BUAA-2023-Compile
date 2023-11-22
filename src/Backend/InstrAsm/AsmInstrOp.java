package Backend.InstrAsm;

public enum AsmInstrOp {
    //Alu
    MULT, DIV,
    ADD, SUB, ADDU, SUBU, ADDI,
    AND, OR,
    SRL, SLL,
    //HILO
    MFHI, MFLO,
    //CMP
    SLT, SLE, SGT, SGE, SEQ, SNE,
    //MEM
    LW, SW,
    //MOVE
    MOVE,
    //SYSCALL
    SYSCALL,
    //LI
    LI,
    //LA
    LA,
    //LABEL
    LABEL,
    //JUMP
    J, JAL, JR,
    //GLOBAL
    WORD, ASCIIZ, SPACE;
    public String toString() {
        return name().toLowerCase();
    }
}

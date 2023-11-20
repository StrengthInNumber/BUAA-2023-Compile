package Middle.LLVMIR.Instruction;

public enum IRInstrType {
    //算数指令
    ADD,// +
    SUB,// -
    MUL,// *
    SDIV,// /
    SREM, //%

    //内存操作
    ALLOCA,
    STORE,
    LOAD,
    GEP,

    //分支跳转
    JUMP,
    BRANCH,

    //逻辑运算
    EQ,
    NE,
    SGT,
    SGE,
    SLT,
    SLE,

    //类型转化
    ZEXT,

    //函数调用
    CALL,
    RET,

    //输入输出
    GETINT,
    PUTINT,
    PUTCH,



    Not, // ! ONLY ONE PARAM
    Beq, // IrBeq branch if ==
    Bne, // IrBne branch if !=
    Blt, // IrBlt branch if less than <
    Ble, // IrBle branch if less or equal <=
    Bgt, // IrBgt branch if greater than >
    Bge, // IrBge branch if greater or equal >=
    Goto, // IrGoto

    //终结指令
    Br,
    Call,
    Ret,
    /* mem op */
    Zext,
    Phi,//用于 mem2reg

    Label,
}

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

    PHI,
    PCOPY,
    MOVE,
}

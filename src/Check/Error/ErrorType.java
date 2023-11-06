package Check.Error;

public enum ErrorType
{
    ILLEGAL_SYMBOL('a'),     // a 非法符号
    REDEFINED_SYMBOL('b'),   // b 名字重定义
    UNDEFINED_SYMBOL('c'),   // c 未定义的名字
    PARAM_NUM_MISMATCH('d'),   // d 函数参数个数不匹配
    PARAM_TYPE_MISMATCH('e'),  // e 函数参数类型不匹配
    VOID_RETURN_VALUE('f'),  // f 无返回值的函数存在不匹配的 return 语句
    MISS_RETURN('g'),        // g 有返回值的函数缺少 return 语句
    CHANGE_CONST_VALUE('h'),       // h 不能改变常量的值
    MISS_SEMICN('i'),     // i 缺少分号
    MISS_RPARENT('j'),       // j 缺少右小括号
    MISS_RBRACK('k'),        // k 缺少右中括号
    FORM_STRING_MISMATCH('l'), // l printf 中格式字符与表达式个数不匹配
    NON_LOOP_STMT('m');     // m 在非循环块中使用 break 和 continue 语句


    private final char typeCode;
    ErrorType(char typeCode){
        this.typeCode = typeCode;
    }

    public char getCode(){
        return typeCode;
    }
}

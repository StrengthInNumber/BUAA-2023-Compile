package Middle.LLVMIR.Type;

import Middle.LLVMIR.IRValue;

public class IRArrayType extends IRType {
    private int eleNum; //数组元素个数
    private IRType eleType; //数组元素类型

    public IRArrayType(int eleNum, IRType eleType){
        this.eleNum = eleNum;
        this.eleType = eleType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(eleNum).append(" x ").append(eleType).append(']');
        return sb.toString();
    }
}

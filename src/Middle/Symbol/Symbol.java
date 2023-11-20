package Middle.Symbol;

import Middle.LLVMIR.IRValue;

public class Symbol {
    private final int lineNum; //from 1
    private final String name;
    private SymbolType type;
    private IRValue value;

    public Symbol(int lineNum, String name, SymbolType type) {
        this.lineNum = lineNum;
        this.name = name;
        this.type = type;
    }

    public void setIRValue(IRValue value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getDim() {
        return 0;
    }

    public ValueType getValueType() {
        return null;
    }

    public IRValue getIRValue() {
        return value;
    }

    public int getLength(int dimensionTh) {
        return 0;
    }
}

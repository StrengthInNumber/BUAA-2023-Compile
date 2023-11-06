package Check.Symbol;

public class Symbol {
    private final int lineNum; //from 1
    private final String name;
    private SymbolType type;
    public Symbol(int lineNum, String name, SymbolType type){
        this.lineNum = lineNum;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getDim(){
        return 0;
    }

    public ValueType getValueType() {
        return null;
    }
}

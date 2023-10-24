package Check.Symbol;

public class SymbolConst extends Symbol {
    private ValueType type;
    private int dim;
    public SymbolConst(int lineNum, String name, int dim, ValueType type){
        super(lineNum, name, SymbolType.SYMBOL_CONST);
        this.dim = dim;
        this.type = type;
    }

    public int getDim(){
        return dim;
    }

    public ValueType getValueType() {
        return type;
    }
}
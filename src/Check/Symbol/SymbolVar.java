package Check.Symbol;

public class SymbolVar extends Symbol{
    private ValueType type;
    private int dim;
    public SymbolVar(int lineNum, String name, int dim, ValueType type){
        super(lineNum, name, SymbolType.SYMBOL_VAR);
        this.dim = dim;
        this.type = type;
    }

    public int getDim() {
        return dim;
    }

    public ValueType getValueType() {
        return type;
    }
}

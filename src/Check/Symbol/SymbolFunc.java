package Check.Symbol;

import java.util.ArrayList;

public class SymbolFunc extends Symbol {
    private ValueType returnType;
    private ArrayList<ValueType> paramTypes;
    private ArrayList<Integer> paramDims;

    public SymbolFunc(int lineNum, String name, ValueType returnType,
                      ArrayList<ValueType> types, ArrayList<Integer> dims) {
        super(lineNum, name, SymbolType.SYMBOL_FUNC);
        paramDims = dims;
        paramTypes = types;
        this.returnType = returnType;
    }

    public int getParamSize(){
        assert paramTypes.size() == paramDims.size();
        return paramDims.size();
    }

    public ArrayList<Integer> getParamDims() {
        return paramDims;
    }

    public ValueType getReturnType() {
        return returnType;
    }

    public int getDim(){
        if(returnType == ValueType.VOID){
            return -1;
        } else {
            return 0;
        }
    }

    public ValueType getValueType() {
        return returnType;
    }
}

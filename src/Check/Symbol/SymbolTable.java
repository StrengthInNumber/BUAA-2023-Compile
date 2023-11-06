package Check.Symbol;

import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> symbols;
    private SymbolTable parent;
    private ValueType returnType; //为函数参数表时存储返回值类型
    private int depth;
    private boolean hasReturned; //当前函数内是否出现 return 语句
    private boolean inCircle; //当前是否在循环语句内
    private ArrayList<ValueType> funcFParamTypes = new ArrayList<>();
    private ArrayList<Integer> funcFParamDims = new ArrayList<>();
    public SymbolTable(int depth){
        symbols = new HashMap<>();
        this.parent = null;
        this.depth = depth;
        hasReturned = false;
        this.inCircle = false;
        returnType = null;
    }

    public SymbolTable(SymbolTable parent){
        this.parent = parent;
        symbols = new HashMap<>();
        this.depth = parent.depth + 1;
        this.hasReturned = false;
        this.inCircle = parent.inCircle;
        this.returnType = parent.returnType;
    }

    public SymbolTable(SymbolTable parent, ValueType returnType){
        this.parent = parent;
        symbols = new HashMap<>();
        this.depth = parent.depth + 1;
        this.hasReturned = false;
        this.inCircle = parent.inCircle;
        this.returnType = returnType;
    }

    public void setInCircle(boolean inCircle) {
        this.inCircle = inCircle;
    }

    public Symbol getSymbol(String name){
        if(symbols.containsKey(name)){
            return symbols.get(name);
        }
        if (parent != null){
            return parent.getSymbol(name);
        } else {
            return null;
        }
    }
    public boolean isInCircle() {
        return inCircle;
    }

    public boolean haveSymbol(String name, boolean isFunc){
        if(symbols.containsKey(name)){
            return true;
        }
        SymbolTable st = parent;
        while(st != null){
            if(st.symbols.containsKey(name)){
                if((st.symbols.get(name) instanceof SymbolVar || st.symbols.get(name) instanceof SymbolConst)
                        && !isFunc){
                    return true;
                } else if (st.symbols.get(name) instanceof SymbolFunc && isFunc){
                    return true;
                }
            }
            st = st.parent;
        }
        return false;
    }
    public void addSymbol(Symbol symbol, boolean isFuncFParam){
        String n = symbol.getName();
        if(isFuncFParam){
            funcFParamDims.add(symbol.getDim());
            funcFParamTypes.add(symbol.getValueType());
        }
        if(symbols.containsKey(n)){
            ErrorTable.getInstance().addError(new Error(symbol.getLineNum(), ErrorType.REDEFINED_SYMBOL));
        } else {
            symbols.put(n, symbol);
        }
    }

    public boolean isVoidFunc(){
        return returnType == ValueType.VOID;
    }

    public void setHasReturned(boolean hasReturned) {
        this.hasReturned = hasReturned;
    }

    public ArrayList<Integer> getFuncFParamDims() { //用于新函数定义读取函数参数维度
        return funcFParamDims;
    }

    public int getFuncDim(String name){
        if(((SymbolFunc)(getSymbol(name))).getReturnType() == ValueType.VOID){
            return -1;
        } else {
            return 0;
        }
    }

    public ArrayList<ValueType> getFuncFParamType() { //用于新函数定义读取函数参数类型
        return funcFParamTypes;
    }

    public boolean isHasReturned() {
        return hasReturned;
    }

    public int getFuncParamSize(String name){
        if(symbols.containsKey(name)){
            return ((SymbolFunc)(symbols.get(name))).getParamSize();
        }
        if (parent != null){
            return parent.getFuncParamSize(name);
        }
        System.out.println("wrong in getFuncParamSize");
        return -1;
    }

    public ArrayList<Integer> getFuncDims(String name){
        if(symbols.containsKey(name)){
            return ((SymbolFunc)(symbols.get(name))).getParamDims();
        }
        if (parent != null){
            return parent.getFuncDims(name);
        }
        System.out.println("wrong in getFuncParamSize");
        return null;
    }
}

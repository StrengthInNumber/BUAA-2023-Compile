package Middle.Symbol;

import java.util.ArrayList;

public class SymbolConst extends Symbol {
    private final ValueType type;
    private final int dim;
    private ArrayList<Integer> lengths; //记录各维数长度
    private int initVal_0; //常量初值
    private ArrayList<Integer> initVal_1 = new ArrayList<>(); //一位数组初值
    private ArrayList<ArrayList<Integer>> initVal_2 = new ArrayList<>(); //一位数组初值

    public SymbolConst(int lineNum, String name, int dim, ValueType type, ArrayList<Integer> lengths) {
        super(lineNum, name, SymbolType.SYMBOL_CONST);
        this.dim = dim;
        this.type = type;
        this.lengths = lengths;
    }

    public SymbolConst(int lineNum, String name, int dim, ValueType type) {
        super(lineNum, name, SymbolType.SYMBOL_CONST);
        this.dim = dim;
        this.type = type;
    }

    public int getLength(int dimensionTh) { //求第 dimensionTh 维数组长度
        return lengths.get(dimensionTh - 1);
    }

    public void setInitVal_0(int initVal_0) {
        this.initVal_0 = initVal_0;
    }

    public void setInitVal_1(ArrayList<Integer> initVal_1) {
        this.initVal_1 = initVal_1;
    }

    public void setInitVal_2(ArrayList<ArrayList<Integer>> initVal_2) {
        this.initVal_2 = initVal_2;
    }

    public int getInitVal_0() {
        return initVal_0;
    }

    public int getInitVal_1(int i) {
        return initVal_1.get(i);
    }

    public int getInitVal_2(int i, int j) {
        return initVal_2.get(i).get(j);
    }

    public int getDim() {
        return dim;
    }

    public ValueType getValueType() {
        return type;
    }
}

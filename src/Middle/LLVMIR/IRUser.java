package Middle.LLVMIR;

import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;

public class IRUser extends IRValue {
    private int opNum;
    protected ArrayList<IRValue> useValues;

    public IRUser(IRType type){
        super(type);
        this.useValues = new ArrayList<>();
    }
    public IRUser(String name, IRType type){
        super(name, type);
        this.useValues = new ArrayList<>();
    }

    public IRUser(IRType type, int opNum){
        super(type);
        this.opNum = opNum;
        this.useValues = new ArrayList<>();
    }
    
    public void addUseValue(IRValue iv) {
        useValues.add(iv);
        if(iv != null) {
            iv.addUse(this);
        }
    }

    public void replaceValue(IRValue oldValue, IRValue newValue) {
        useValues.set(useValues.indexOf(oldValue), newValue);
        oldValue.removeUse(this);
        newValue.addUse(this);
    }
}

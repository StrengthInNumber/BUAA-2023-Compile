package Middle.LLVMIR;

import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;

public class IRUser extends IRValue {
    private int opNum;
    protected ArrayList<IRValue> operands;

    public IRUser(IRType type){
        super(type);
        this.operands = new ArrayList<>();
    }
    public IRUser(String name, IRType type){
        super(name, type);
        this.operands = new ArrayList<>();
    }

    public IRUser(IRType type, int opNum){
        super(type);
        this.opNum = opNum;
        this.operands = new ArrayList<>();
    }
}

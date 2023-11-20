package Middle.LLVMIR;

import Middle.LLVMIR.Type.IRType;

import java.util.ArrayList;

public class IRValue {
    protected IRType type;
    protected String name;
    private ArrayList<IRUse> uses;
    public IRValue(String name, IRType type){
        this.type = type;
        this.name = name;
        uses = new ArrayList<>();
    }

    public IRValue(IRType type){
        this.type = type;
        this.name = "";
        uses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public IRType getType() {
        return type;
    }
}

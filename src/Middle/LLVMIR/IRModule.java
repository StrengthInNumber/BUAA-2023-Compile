package Middle.LLVMIR;

import Backend.InstrAsm.AsmInstrJump;
import Backend.InstrAsm.AsmInstrLabel;
import Backend.InstrAsm.AsmInstrOp;
import Frontend.Parser.CompUnit;
import Frontend.Parser.DeclAndDef.Decl;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.GlobalVar.IRGlobalVar;
import Middle.LLVMIR.Type.IROtherType;

import java.util.ArrayList;

public class IRModule extends IRValue {
    private ArrayList<IRGlobalVar> globalVars;
    private ArrayList<IRFunction> functions;
    private ArrayList<IRString> strings;

    public IRModule(){
        super(IROtherType.MODULE);
        globalVars = new ArrayList<>();
        functions = new ArrayList<>();
        strings = new ArrayList<>();
    }

    public void addGlobalVar(IRGlobalVar gv){
        globalVars.add(gv);
    }

    public void addFunction(IRFunction f){
       functions.add(f);
    }

    public void addIRString(IRString s) {
        strings.add(s);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("declare i32 @getint()\n" +
                "declare void @putint(i32)\n" +
                "declare void @putch(i32)\n");
        for(IRGlobalVar gv : globalVars){
            sb.append(gv);
        }
        for(IRFunction f : functions){
            sb.append(f);
        }
        return sb.toString();
    }

    public void generateAsm() {
        for(IRGlobalVar gv : globalVars) {
            gv.generateAsm();
        }
        for(IRString s : strings) {
            s.generateAsm();
        }
        new AsmInstrJump(AsmInstrOp.JAL, "main");
        new AsmInstrJump(AsmInstrOp.J, "END");
        for(IRFunction f :functions){
            f.generateAsm();
        }
        new AsmInstrLabel("END");
    }
}

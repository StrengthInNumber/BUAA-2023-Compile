package Frontend.Parser;

import Middle.CompilerError;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRModule;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.DeclAndDef.Decl;
import Frontend.Parser.DeclAndDef.Function.FuncDef;
import Frontend.Parser.DeclAndDef.Function.MainFuncDef;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class CompUnit extends ASTNode {
    // 编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef // 1.是否存在Decl 2.是否存在FuncDef
    private ArrayList<Decl> decls;
    private ArrayList<FuncDef> funcDefs;
    private MainFuncDef mainFuncDef;
    private SymbolTable symbolTable_error;
    private SymbolTable symbolTable_ir;

    public CompUnit(TokensReadControl tokens) {
        super(tokens);
        decls = new ArrayList<>();
        funcDefs = new ArrayList<>();
        symbolTable_error = new SymbolTable(0);
        symbolTable_ir = new SymbolTable(0);
    }

    public ArrayList<Decl> getDecls() {
        return decls;
    }

    public ArrayList<FuncDef> getFuncDefs() {
        return funcDefs;
    }

    public MainFuncDef getMainFuncDef() {
        return mainFuncDef;
    }

    public void parse() throws CompilerError {
        //{Decl}
        TokenType t2 = tokens.getPreTokenType(1);
        TokenType t3 = tokens.getPreTokenType(2);
        while (t3 != TokenType.LPARENT) {
            Decl decl = new Decl(tokens);
            decl.parse();
            decls.add(decl);
            t2 = tokens.getPreTokenType(1);
            t3 = tokens.getPreTokenType(2);
        }
        //{FuncDef}
        while (t2 != TokenType.MAINTK) {
            FuncDef funcDef = new FuncDef(tokens);
            funcDef.parse();
            funcDefs.add(funcDef);
            t2 = tokens.getPreTokenType(1);
        }
        //MainFuncDef
        mainFuncDef = new MainFuncDef(tokens);
        mainFuncDef.parse();
    }

    public void checkError(){
        for(Decl decl: decls){
            decl.checkError(symbolTable_error);
        }
        for(FuncDef funcDef: funcDefs){
            funcDef.checkError(symbolTable_error);
        }
        mainFuncDef.checkError(symbolTable_error);
    }

    public void generateIR() {
        IRModule irModule = new IRModule();
        IRBuilder.getInstance().setCurModule(irModule);
        for(Decl decl: decls){
            decl.generateIRGlobal(symbolTable_ir);
        }
        for(FuncDef funcDef: funcDefs){
            funcDef.generateIR(symbolTable_ir);
        }
        mainFuncDef.generateIR(symbolTable_ir);
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Decl decl: decls){
            sb.append(decl.toString());
        }
        for(FuncDef funcDef: funcDefs){
            sb.append(funcDef.toString());
        }
        sb.append(mainFuncDef.toString());
        sb.append("<CompUnit>\n");
        return sb.toString();
    }
}


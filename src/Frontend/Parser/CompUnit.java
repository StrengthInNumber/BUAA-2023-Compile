package Frontend.Parser;

import Check.CompilerError;
import Frontend.Lexer.Token.Token;
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

    public CompUnit(TokensReadControl tokens) {
        super(tokens);
        decls = new ArrayList<>();
        funcDefs = new ArrayList<>();
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
}

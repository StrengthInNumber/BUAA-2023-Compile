package Frontend.Parser.DeclAndDef.Constant;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.BType;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class ConstDecl extends ASTNode {
    //常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    // 1.花括号内重复0 次 2.花括号内重复多次
    private BType bType;
    private ArrayList<ConstDef> constDefs;

    public ConstDecl(TokensReadControl tokens){
        super(tokens);
        constDefs = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        TokenType tt = tokens.getNowTokenType();
        if(tt != TokenType.CONSTTK){
            printError();
        }
        tokens.nextToken();
        tt = tokens.getNowTokenType();
        if(tt != TokenType.INTTK){
            printError();
        }
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        ConstDef constDef = new ConstDef(tokens);
        constDef.parse();
        constDefs.add(constDef);
        tt = tokens.getNowTokenType();
        while(tt == TokenType.COMMA){
            constDef = new ConstDef(tokens);
            constDef.parse();
            constDefs.add(constDef);
            tt = tokens.getNowTokenType();
        }
        if(tt != TokenType.SEMICN){
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
    }
}

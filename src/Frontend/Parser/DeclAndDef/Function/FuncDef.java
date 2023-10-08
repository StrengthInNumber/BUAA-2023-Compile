package Frontend.Parser.DeclAndDef.Function;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Block;
import Frontend.Parser.Terminator.FuncType;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

public class FuncDef extends ASTNode {
    //函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    // 1.无形参 2.有形参
    private FuncType funcType;
    private Ident ident;
    private FuncFParams funcFParams;
    private int flag; //0-no params 1-have params
    private Block block;
    public FuncDef(TokensReadControl tokens){
        super(tokens);
        flag = 0;
    }
    public void parse() throws CompilerError {
        funcType = new FuncType(tokens.getNowToken());
        tokens.nextToken();
        ident = new Ident(tokens.getNowToken());
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() == TokenType.INTTK){
            funcFParams = new FuncFParams(tokens);
            funcFParams.parse();
        }
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
        block = new Block(tokens);
        block.parse();
    }
}

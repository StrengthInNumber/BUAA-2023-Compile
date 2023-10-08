package Frontend.Parser.DeclAndDef.Function;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Block;
import Frontend.TokensReadControl;

public class MainFuncDef extends ASTNode {
    //主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block
    // 存在main函数
    private Block block;
    public MainFuncDef(TokensReadControl tokens){
        super(tokens);
    }
    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() != TokenType.INTTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.MAINTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
        block = new Block(tokens);
        block.parse();
    }
}

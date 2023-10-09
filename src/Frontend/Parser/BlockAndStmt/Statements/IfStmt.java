package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Stmt;
import Frontend.Parser.Expression.Cond;
import Frontend.TokensReadControl;

public class IfStmt extends ASTNode implements StmtOpt{
    //IfStmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    // 1.有else 2.无else
    private Cond cond;
    private Stmt stmt;
    private Stmt elseStmt;
    private int flag; //0-no else  1-have else
    public IfStmt(TokensReadControl tokens){
        super(tokens);
        flag = 0;
        cond = new Cond(tokens);
        stmt = new Stmt(tokens);
    }

    public void parse() throws CompilerError{
        if(tokens.getNowTokenType() != TokenType.IFTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        cond.parse();
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
        stmt.parse();
        if(tokens.getNowTokenType() == TokenType.ELSETK){
            tokens.nextToken();
            flag = 1;
            elseStmt = new Stmt(tokens);
            elseStmt.parse();
        }
    }
}

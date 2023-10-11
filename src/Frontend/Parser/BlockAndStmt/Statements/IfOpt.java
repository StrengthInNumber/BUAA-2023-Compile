package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Stmt;
import Frontend.Parser.Expression.Cond;
import Frontend.TokensReadControl;

public class IfOpt extends ASTNode implements StmtOpt{
    //IfStmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    // 1.有else 2.无else
    private Cond cond;
    private Stmt stmt;
    private Stmt elseStmt;
    private int flag; //0-no else  1-have else
    public IfOpt(TokensReadControl tokens){
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
        } else {
            tokens.nextToken();
        }
        stmt.parse();
        if(tokens.getNowTokenType() == TokenType.ELSETK){
            tokens.nextToken();
            flag = 1;
            elseStmt = new Stmt(tokens);
            elseStmt.parse();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("IFTK if\n");
        sb.append("LPARENT (\n");
        sb.append(cond);
        sb.append("RPARENT )\n");
        sb.append(stmt);
        if(flag == 1){
            sb.append("ELSETK else\n");
            sb.append(elseStmt);
        }
        return sb.toString();
    }
}

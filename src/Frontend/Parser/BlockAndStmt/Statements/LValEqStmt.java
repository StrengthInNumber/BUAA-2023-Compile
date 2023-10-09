package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.LVal;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

public class LValEqStmt extends ASTNode implements StmtOpt {
    private LVal lVal;
    private Exp exp;
    private int flag; //0- LVal '=' Exp ';'      1- LVal '=' 'getint''('')'';'
    public LValEqStmt(TokensReadControl tokens){
        super(tokens);
        lVal = new LVal(tokens);
    }

    public void parse() throws CompilerError {
        lVal.parse();
        if(tokens.getNowTokenType() != TokenType.ASSIGN){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() == TokenType.GETINTTK){
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.LPARENT){
                printError();
            }
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.RPARENT){
                throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            }
            tokens.nextToken();
        } else {
            exp = new Exp(tokens);
            exp.parse();
        }
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
    }
}

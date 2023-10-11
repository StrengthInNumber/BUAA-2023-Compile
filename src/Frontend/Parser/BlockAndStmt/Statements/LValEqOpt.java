package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.LVal;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

public class LValEqOpt extends ASTNode implements StmtOpt {
    private LVal lVal;
    private Exp exp;
    private int flag; //0- LVal '=' Exp ';'      1- LVal '=' 'getint''('')'';'
    public LValEqOpt(TokensReadControl tokens){
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
            flag = 1;
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.LPARENT){
                printError();
            }
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.RPARENT){
                throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            } else {
                tokens.nextToken();
            }
        } else {
            flag = 0;
            exp = new Exp(tokens);
            exp.parse();
        }
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(lVal);
        sb.append("ASSIGN =\n");
        if(flag == 0){
            sb.append(exp);
        } else {
            sb.append("GETINTTK getint\n");
            sb.append("LPARENT (\n");
            sb.append("RPARENT )\n");
        }
        sb.append("SEMICN ;\n");
        return sb.toString();
    }
}

package Frontend.Parser.BlockAndStmt;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Statements.*;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

public class Stmt extends ASTNode {
    //Stmt → LValEqExp | ExpOpt | Block | IfOpt |
    // ForOpt | BreakOpt | ContinueOpt | ReturnOpt | LValEqGetint ｜ PrintfOpt
    private StmtOpt stmtOpt;
    public Stmt(TokensReadControl tokens){
        super(tokens);
    }

    public void parse() throws CompilerError {
        switch (tokens.getNowTokenType()){
            case IDENFR:
                int check = tokens.getNowIndex();
                Exp temp = new Exp(tokens);
                temp.parse();
                if(tokens.getNowTokenType() == TokenType.ASSIGN){
                    tokens.setNowIndex(check);
                    stmtOpt = new LValEqOpt(tokens);
                } else {
                    tokens.setNowIndex(check);
                    stmtOpt = new ExpOpt(tokens);
                }
                break;
            case LBRACE:
                stmtOpt = new BlockOpt(tokens);
                break;
            case IFTK:
                stmtOpt = new IfOpt(tokens);
                break;
            case FORTK:
                stmtOpt = new ForOpt(tokens);
                break;
            case BREAKTK:
                stmtOpt = new BreakOpt(tokens);
                break;
            case CONTINUETK:
                stmtOpt = new ContinueOpt(tokens);
                break;
            case RETURNTK:
                stmtOpt = new ReturnOpt(tokens);
                break;
            case PRINTFTK:
                stmtOpt = new PrintfOpt(tokens);
                break;
            default:
                stmtOpt = new ExpOpt(tokens);
                break;
        }
        stmtOpt.parse();
    }
    public void checkError(SymbolTable table){
        stmtOpt.checkError(table);
    }

    public boolean isReturnStmt(){
        return stmtOpt instanceof ReturnOpt;
    }
    public String toString(){
        return stmtOpt.toString() + "<Stmt>\n";
    }
}

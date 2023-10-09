package Frontend.Parser.BlockAndStmt;

import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Statements.LValEqStmt;
import Frontend.Parser.BlockAndStmt.Statements.StmtOpt;
import Frontend.TokensReadControl;

public class Stmt extends ASTNode {
    //Stmt → LValEqExp | ExpsStmt | Block | IfStmt | ForStmt |
    // BreakStmt | ContinueStmt | ReturnStmt | LValEqGetint ｜ PrintfStmt
    private StmtOpt stmtOpt;
    public Stmt(TokensReadControl tokens){
        super(tokens);
    }

    public void parse(){
        switch (tokens.getNowTokenType()){
            case TokenType.IDENFR:
                stmtOpt = new LValEqStmt(tokens);
                stmtOpt.parse();
        }
    }
}

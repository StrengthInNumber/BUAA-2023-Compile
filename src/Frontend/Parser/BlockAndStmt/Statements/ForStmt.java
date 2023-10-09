package Frontend.Parser.BlockAndStmt.Statements;

import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class ForStmt extends ASTNode implements StmtOpt{
    //ForStmt → 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    // 1. 无缺省 2. 缺省第一个 ForStmt 3. 缺省Cond 4. 缺省第二个ForStmt

    public ForStmt(TokensReadControl tokens){
        super(tokens);
    }
}

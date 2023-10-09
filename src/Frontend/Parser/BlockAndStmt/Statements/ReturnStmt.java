package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

public class ReturnStmt extends ASTNode implements StmtOpt {
    //ReturnStmt → 'return' [Exp] ';'
    // 1.有Exp 2.无Exp
    private Exp exp;

    public ReturnStmt(TokensReadControl tokens) {
        super(tokens);
        exp = null;
    }

    public void parse() throws CompilerError {
        if (tokens.getNowTokenType() != TokenType.RETURNTK) {
            printError();
        }
        tokens.nextToken();
        exp = new Exp(tokens);
        exp.parse();
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
    }
}

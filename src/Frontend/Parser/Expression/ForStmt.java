package Frontend.Parser.Expression;

import Check.CompilerError;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class ForStmt extends ASTNode {
    private LVal lVal;
    private Exp exp;

    public ForStmt(TokensReadControl tokens) {
        super(tokens);
        lVal = new LVal(tokens);
        exp = new Exp(tokens);
    }

    public void parse() throws CompilerError {
        lVal.parse();
        if (tokens.getNowTokenType() != TokenType.ASSIGN) {
            printError();
        }
        tokens.nextToken();
        exp.parse();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal);
        sb.append("ASSIGN =\n");
        sb.append(exp);
        sb.append("<ForStmt>\n");
        return sb.toString();
    }
}

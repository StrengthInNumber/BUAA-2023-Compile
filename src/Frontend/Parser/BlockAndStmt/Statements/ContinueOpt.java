package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class ContinueOpt extends ASTNode implements StmtOpt{
    //ContinueStmt → "continue" ";"
    private Token continueTK;

    public ContinueOpt(TokensReadControl tokens) {
        super(tokens);
    }

    public void parse() throws CompilerError {
        if (tokens.getNowTokenType() != TokenType.CONTINUETK) {
            printError();
        }
        continueTK = tokens.getNowToken();
        tokens.nextToken();
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
    }

    public String toString(){
        return "CONTINUETK continue\n" + "SEMICN ;\n";
    }
}

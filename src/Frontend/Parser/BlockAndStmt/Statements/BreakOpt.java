package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class BreakOpt extends ASTNode implements StmtOpt {
    //BreakStmt → 'break' ';'
    private Token breakTK;

    public BreakOpt(TokensReadControl tokens) {
        super(tokens);
    }

    public void parse() throws CompilerError {
        if (tokens.getNowTokenType() != TokenType.BREAKTK) {
            printError();
        }
        breakTK = tokens.getNowToken();
        tokens.nextToken();
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
    }

    public String toString(){
        return "BREAKTK break\nSEMICN ;\n";
    }
}

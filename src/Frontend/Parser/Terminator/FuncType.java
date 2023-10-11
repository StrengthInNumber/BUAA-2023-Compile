package Frontend.Parser.Terminator;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class FuncType {
    private Token token;

    public FuncType(Token token) {
        this.token = token;
        assert token.getType() == TokenType.VOIDTK || token.getType() == TokenType.INTTK;
    }

    public String toString() {
        return token.toString() + "<FuncType>\n";
    }
}

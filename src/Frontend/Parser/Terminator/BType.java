package Frontend.Parser.Terminator;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class BType {
    private Token token;
    public BType(Token token){
        this.token = token;
        assert token.getType() == TokenType.INTTK;
    }

    public String toString() {
        return token.toString();
    }
}

package Frontend.Parser.Terminator;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class Ident {
    private Token token;

    public Ident(Token token) {
        this.token = token;
        assert token.getType() == TokenType.IDENFR;
    }

    public String getName(){
        return token.getContent();
    }
    public String toString() {
        return token.toString();
    }
}

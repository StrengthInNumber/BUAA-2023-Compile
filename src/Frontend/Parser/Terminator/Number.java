package Frontend.Parser.Terminator;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class Number {
    private Token intConst;

    public Number(Token token) {
        assert token.getType() == TokenType.INTCON;
        intConst = token;
    }

    public String toString() {
        return intConst.toString() + "<Number>\n";
    }

}

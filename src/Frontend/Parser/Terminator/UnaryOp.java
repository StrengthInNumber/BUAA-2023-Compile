package Frontend.Parser.Terminator;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class UnaryOp {
    private Token option;

    public UnaryOp(Token token) {
        assert token.getType() == TokenType.PLUS
                || token.getType() == TokenType.MINU
                || token.getType() == TokenType.NOT;
        option = token;
    }

    public boolean isPlus() {
        return option.getType() == TokenType.PLUS;
    }

    public boolean isMinus() {
        return option.getType() == TokenType.MINU;
    }
    public String toString() {
        return option.toString() + "<UnaryOp>\n";
    }
}

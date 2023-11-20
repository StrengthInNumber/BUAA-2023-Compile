package Frontend.Parser.Terminator;

import Middle.Symbol.ValueType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class BType {
    private Token token;
    private ValueType valueType;
    public BType(Token token){
        this.token = token;
        assert token.getType() == TokenType.INTTK;
        valueType = ValueType.INT;
    }

    public String toString() {
        return token.toString();
    }

    public ValueType getValueType() {
        return valueType;
    }
}

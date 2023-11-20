package Frontend.Parser.Terminator;

import Middle.Symbol.ValueType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

public class FuncType {
    private ValueType valueType;
    private Token token;

    public FuncType(Token token) {
        this.token = token;
        assert token.getType() == TokenType.VOIDTK || token.getType() == TokenType.INTTK;
        if(token.getType() == TokenType.VOIDTK){
            valueType = ValueType.VOID;
        } else if (token.getType() == TokenType.INTTK){
            valueType = ValueType.INT;
        }
    }

    public String toString() {
        return token.toString() + "<FuncType>\n";
    }

    public ValueType getValueType() {
        return valueType;
    }
}

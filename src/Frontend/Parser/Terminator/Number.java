package Frontend.Parser.Terminator;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Type.IRIntegerType;

public class Number {
    private Token intConst;

    public Number(Token token) {
        assert token.getType() == TokenType.INTCON;
        intConst = token;
    }

    public int getConstValue() {
        return Integer.valueOf(intConst.getContent());
    }

    public IRValue generateIR() {
        return new IRConstInt(IRIntegerType.INT32, Integer.valueOf(intConst.getContent()));
    }
    public String toString() {
        return intConst.toString() + "<Number>\n";
    }

}

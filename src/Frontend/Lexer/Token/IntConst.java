package Frontend.Lexer.Token;

public class IntConst extends Token{
    private final int value;
    public IntConst(int lineNum, String s){
        super(TokenType.INTCON, lineNum, s);
        value = Integer.parseInt(s);
    }
}

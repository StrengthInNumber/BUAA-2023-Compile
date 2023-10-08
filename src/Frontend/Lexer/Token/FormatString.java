package Frontend.Lexer.Token;

public class FormatString extends Token{
    public FormatString(int lineNum, String content){
        super(TokenType.STRCON, lineNum, content);
    }
}

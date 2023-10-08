package Frontend.Lexer.Token;

public class ReservedWord extends Token{
    public ReservedWord(TokenType type, int lineNum, String content){
        super(type, lineNum, content);
    }
}

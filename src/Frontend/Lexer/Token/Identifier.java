package Frontend.Lexer.Token;

public class Identifier extends Token{
    public Identifier(int lineNum, String content){
        super(TokenType.IDENFR, lineNum, content);
    }
}

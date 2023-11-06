package Frontend.Lexer.Token;

public class Token {
    private final TokenType type;
    private final int lineNum;
    private final String content;

    public Token(TokenType type, int lineNum, String content){
        this.type = type;
        this.lineNum = lineNum;
        this.content = content;
    }

    public TokenType getType() {
        return type;
    }
    public int getLineNum(){
        return lineNum;
    }

    public String getContent() {
        return content;
    }

    public String toString(){
        return type.toString() + ' ' + content + '\n';
    }
}

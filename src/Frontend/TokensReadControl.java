package Frontend;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;

import java.util.ArrayList;


public class TokensReadControl {
    private ArrayList<Token> tokens;
    private int nowIndex;

    public TokensReadControl(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public void nextToken(){
        nowIndex++;
    }

    public Token getNowToken(){
        return tokens.get(nowIndex);
    }
    public TokenType getPreTokenType(int num){
        return tokens.get(nowIndex + num).getType();
    }
    public TokenType getNowTokenType(){
        return tokens.get(nowIndex).getType();
    }
    public boolean tokensHasNext(){
        return nowIndex < tokens.size();
    }
    public int getNowTokenLineNum(){
        return tokens.get(nowIndex).getLineNum();
    }

    public int getNowIndex() {
        return nowIndex;
    }

    public void setNowIndex(int nowIndex) {
        this.nowIndex = nowIndex;
    }

    public int getLastTokenLineNum() {
        return tokens.get(nowIndex - 1).getLineNum();
    }
}

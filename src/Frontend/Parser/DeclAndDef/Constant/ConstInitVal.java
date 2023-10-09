package Frontend.Parser.DeclAndDef.Constant;

import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.AddExp;
import Frontend.Parser.Expression.ConstExp;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class ConstInitVal extends ASTNode {
    //常量初值 ConstInitVal → ConstExp| '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
    //ConstExp即为AddExp
    // 1.常表达式初值 2.一维数组初值 3.二维数组初值
    private ConstExp constExp;
    private ArrayList<ConstInitVal> constInitVals;
    private int flag; //0-exp 1-vals
    public ConstInitVal(TokensReadControl tokens){
        super(tokens);
        constInitVals = new ArrayList<>();
        flag = 0;
    }
    public void parse(){
        if(tokens.getNowTokenType() == TokenType.LBRACE){
            tokens.nextToken();
            flag = 1;
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                ConstInitVal constInitVal = new ConstInitVal(tokens);
                constInitVal.parse();
                constInitVals.add(constInitVal);
                while(tokens.getNowTokenType() == TokenType.COMMA){
                    constInitVal = new ConstInitVal(tokens);
                    constInitVal.parse();
                    constInitVals.add(constInitVal);
                }
            }
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                printError();
            }
            tokens.nextToken();
        } else {
            constExp = new ConstExp(tokens);
            constExp.parse();
            flag = 0;
        }
    }
}

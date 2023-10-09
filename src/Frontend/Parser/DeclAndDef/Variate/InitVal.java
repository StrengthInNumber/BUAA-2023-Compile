package Frontend.Parser.DeclAndDef.Variate;

import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.DeclAndDef.Constant.ConstInitVal;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class InitVal extends ASTNode {
    //变量初值 InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    // 1.表达式初值 2.一维数 组初值 3.二维数组初值
    private Exp exp;
    private ArrayList<InitVal> initVals;
    private int flag; //0-exp 1-vals
    public InitVal(TokensReadControl tokens){
        super(tokens);
        initVals = new ArrayList<>();
        flag = 0;
    }
    public void parse(){
        if(tokens.getNowTokenType() == TokenType.LBRACE){
            tokens.nextToken();
            flag = 1;
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                InitVal initVal = new InitVal(tokens);
                initVal.parse();
                initVals.add(initVal);
                while(tokens.getNowTokenType() == TokenType.COMMA){
                    initVal = new InitVal(tokens);
                    initVal.parse();
                    initVals.add(initVal);
                }
            }
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                printError();
            }
            tokens.nextToken();
        } else {
            exp = new ConstExp(tokens);
            exp.parse();
            flag = 0;
        }
    }
}

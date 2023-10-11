package Frontend.Parser.DeclAndDef.Variate;

import Check.CompilerError;
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
    private InitVal initVal;
    private ArrayList<InitVal> initVals;
    private int flag; //0-exp 1-vals
    public InitVal(TokensReadControl tokens){
        super(tokens);
        initVals = new ArrayList<>();
        flag = 0;
    }
    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() == TokenType.LBRACE){
            tokens.nextToken();
            flag = 1;
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                initVal = new InitVal(tokens);
                initVal.parse();
                while(tokens.getNowTokenType() == TokenType.COMMA){
                    tokens.nextToken();
                    InitVal i = new InitVal(tokens);
                    i.parse();
                    initVals.add(i);
                }
            }
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                printError();
            }
            tokens.nextToken();
        } else {
            exp = new Exp(tokens);
            exp.parse();
            flag = 0;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(flag == 0){
            sb.append(exp);
        } else {
            sb.append("LBRACE {\n");
            if(initVal != null){
                sb.append(initVal);
                for(InitVal i : initVals){
                    sb.append("COMMA ,\n");
                    sb.append(i);
                }
            }
            sb.append("RBRACE }\n");
        }
        sb.append("<InitVal>\n");
        return sb.toString();
    }
}

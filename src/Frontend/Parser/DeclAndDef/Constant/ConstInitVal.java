package Frontend.Parser.DeclAndDef.Constant;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
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
    private ConstInitVal constInitVal;
    private ArrayList<ConstInitVal> constInitVals;
    private int flag; //0-exp 1-vals
    public ConstInitVal(TokensReadControl tokens){
        super(tokens);
        constInitVal = null;
        constInitVals = new ArrayList<>();
        flag = 0;
    }
    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() == TokenType.LBRACE){
            tokens.nextToken();
            flag = 1;
            if(tokens.getNowTokenType() != TokenType.RBRACE){
                constInitVal = new ConstInitVal(tokens);
                constInitVal.parse();
                while(tokens.getNowTokenType() == TokenType.COMMA){
                    tokens.nextToken();
                    ConstInitVal ci = new ConstInitVal(tokens);
                    ci.parse();
                    constInitVals.add(ci);
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

    public void checkError(SymbolTable table){
        if(flag == 0){
            constExp.checkError(table);
        } else {
            if(constInitVal != null){
                constInitVal.checkError(table);
                for(ConstInitVal ci : constInitVals){
                    ci.checkError(table);
                }
            }
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(flag == 0){
            sb.append(constExp);
        } else {
            sb.append("LBRACE {\n");
            if(constInitVal != null){
                sb.append(constInitVal);
                for(ConstInitVal ci : constInitVals){
                    sb.append("COMMA ,\n");
                    sb.append(ci);
                }
            }
            sb.append("RBRACE }\n");
        }
        sb.append("<ConstInitVal>\n");
        return sb.toString();
    }
}

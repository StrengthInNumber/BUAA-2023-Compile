package Frontend.Parser.DeclAndDef.Variate;

import Frontend.Parser.DeclAndDef.Constant.ConstInitVal;
import Middle.CompilerError;
import Middle.LLVMIR.IRValue;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
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

    public void checkError(SymbolTable table){
        if(flag == 0){
            exp.checkError(table);
        } else {
            if(initVal != null) {
                initVal.checkError(table);
                for (InitVal i : initVals) {
                    i.checkError(table);
                }
            }
        }
    }
    public int getConstValue_0(SymbolTable table){
        if(flag == 0){
            return exp.getConstValue(table);
        } else {
            System.out.println("wrong in ConstInitVal.getConstValue_0");
            return -9999;
        }
    }

    public ArrayList<Integer> getConstValue_1(SymbolTable table){
        ArrayList<Integer> ans = new ArrayList<>();
        ans.add(initVal.getConstValue_0(table));
        for(InitVal c : initVals){
            ans.add(c.getConstValue_0(table));
        }
        return ans;
    }

    public ArrayList<ArrayList<Integer>> getConstValue_2(SymbolTable table){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        ans.add(initVal.getConstValue_1(table));
        for(InitVal c : initVals){
            ans.add(c.getConstValue_1(table));
        }
        return ans;
    }

    public IRValue getInitValue_0(SymbolTable table) {
        return exp.generateIR(table);
    }

    public ArrayList<IRValue> getInitValue_1(SymbolTable table) {
        ArrayList<IRValue> values = new ArrayList<>();
        values.add(initVal.getInitValue_0(table));
        for(InitVal i : initVals) {
            values.add(i.getInitValue_0(table));
        }
        return values;
    }

    public ArrayList<IRValue> getInitValue_2(SymbolTable table) {
        ArrayList<IRValue> values = new ArrayList<>(initVal.getInitValue_1(table));
        for(InitVal i : initVals) {
            values.addAll(i.getInitValue_1(table));
        }
        return values;
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

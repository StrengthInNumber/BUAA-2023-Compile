package Frontend.Parser.DeclAndDef.Function;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.ConstExp;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncFParams extends ASTNode {
    //函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
    // 1.花括号内重复0次 2.花括号内 重复多次
    private FuncFParam funcFParam;
    private ArrayList<FuncFParam> funcFParams;
    public FuncFParams(TokensReadControl tokens){
        super(tokens);
        funcFParams = new ArrayList<>();
        funcFParam = new FuncFParam(tokens);
    }

    public void parse() throws CompilerError {
        funcFParam.parse();
        while(tokens.getNowTokenType() == TokenType.COMMA){
            tokens.nextToken();
            FuncFParam f = new FuncFParam(tokens);
            f.parse();
            funcFParams.add(f);
        }
    }

    public void checkError(SymbolTable table){
        funcFParam.checkError(table);
        for(FuncFParam f : funcFParams){
            f.checkError(table);
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(funcFParam);
        for(FuncFParam f : funcFParams){
            sb.append("COMMA ,\n");
            sb.append(f);
        }
        sb.append("<FuncFParams>\n");
        return sb.toString();
    }
}

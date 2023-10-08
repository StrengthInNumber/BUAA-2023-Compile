package Frontend.Parser.DeclAndDef.Function;

import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncFParams extends ASTNode {
    //函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
    // 1.花括号内重复0次 2.花括号内 重复多次
    private ArrayList<FuncFParam> funcFParams;
    public FuncFParams(TokensReadControl tokens){
        super(tokens);
        funcFParams = new ArrayList<>();
    }

    public void parse(){
        FuncFParam funcFParam = new FuncFParam(tokens);
        funcFParam.parse();
        funcFParams.add(funcFParam);
        while(tokens.getNowTokenType() == TokenType.COMMA){
            funcFParam = new FuncFParam(tokens);
            funcFParam.parse();
            funcFParams.add(funcFParam);
        }
    }
}

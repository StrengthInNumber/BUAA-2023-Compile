package Frontend.Parser.Expression;

import Check.CompilerError;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.DeclAndDef.Function.FuncFParam;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncRParams extends ASTNode {
    //函数实参表 FuncRParams → Exp { ',' Exp }
    // 1.花括号内重复0次 2.花括号内重复多次 3.Exp需 要覆盖数组传参和部分数组传参
    private Exp exp;
    private ArrayList<Exp> exps;

    public FuncRParams(TokensReadControl tokens) {
        super(tokens);
        exps = new ArrayList<>();
        exp = new Exp(tokens);
    }

    public void parse() throws CompilerError {
        exp.parse();
        while (tokens.getNowTokenType() == TokenType.COMMA) {
            tokens.nextToken();
            Exp e = new Exp(tokens);
            e.parse();
            exps.add(e);
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(exp);
        for(Exp e : exps){
            sb.append("COMMA ,\n");
            sb.append(e);
        }
        sb.append("<FuncRParams>\n");
        return sb.toString();
    }
}

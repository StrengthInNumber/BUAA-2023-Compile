package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.Number;
import Frontend.TokensReadControl;

public class PrimaryExp extends ASTNode {
    //基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
    // 三种情况均需覆盖
    private Exp exp;
    private LVal lVal;
    private Number number;
    private int flag; //0-exp 1-LVal 2-Number

    public PrimaryExp(TokensReadControl tokens) {
        super(tokens);
        flag = 0;
    }

    public void parse() throws CompilerError {
        switch (tokens.getNowTokenType()) {
            case TokenType.LPARENT:
                tokens.nextToken();
                exp = new Exp(tokens);
                flag = 0;
                exp.parse();
                if (tokens.getNowTokenType() != TokenType.RPARENT) {
                    throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
                } else {
                    tokens.nextToken();
                }
                break;
            case TokenType.IDENFR:
                lVal = new LVal(tokens);
                flag = 1;
                lVal.parse();
                break;
            case TokenType.INTCON:
                number = new Number(tokens.getNowToken());
                flag = 2;
                tokens.nextToken();
                break;
            default:
                printError();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        switch (flag){
            case 0:
                sb.append("LPARENT (\n");
                sb.append(exp);
                sb.append("RPARENT )\n");
                break;
            case 1:
                sb.append(lVal);
                break;
            case 2:
                sb.append(number);
                break;
        }
        sb.append("<PrimaryExp>\n");
        return sb.toString();
    }
}

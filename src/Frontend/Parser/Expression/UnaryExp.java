package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.DeclAndDef.Function.FuncFParams;
import Frontend.Parser.Terminator.Ident;
import Frontend.Parser.Terminator.UnaryOp;
import Frontend.TokensReadControl;

import java.util.HashSet;

public class UnaryExp extends ASTNode {
    //一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    // 3种情况均需覆盖,函 数调用也需要覆盖FuncRParams的不同情况
    private int flag; //0-primaryExp 1-Ident[][] 2-Unary
    private PrimaryExp primaryExp;
    private Ident ident;
    private FuncRParams funcRParams;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public UnaryExp(TokensReadControl tokens) {
        super(tokens);
        funcRParams = null;
        flag = 0;
    }

    public void parse() throws CompilerError {
        TokenType t1 = tokens.getNowTokenType();
        TokenType t2 = tokens.getPreTokenType(1);
        if (t1 == TokenType.PLUS || t1 == TokenType.MINU || t1 == TokenType.NOT) {
            unaryOp = new UnaryOp(tokens.getNowToken());
            flag = 2;
            tokens.nextToken();
            unaryExp = new UnaryExp(tokens);
            unaryExp.parse();
        } else if (t2 == TokenType.LPARENT) {
            ident = new Ident(tokens.getNowToken());
            flag = 1;
            tokens.nextToken();
            tokens.nextToken();
            HashSet<TokenType> expFIRST = new HashSet<>();
            expFIRST.add(TokenType.PLUS);
            expFIRST.add(TokenType.MINU);
            expFIRST.add(TokenType.NOT);
            expFIRST.add(TokenType.LPARENT);
            expFIRST.add(TokenType.IDENFR);
            expFIRST.add(TokenType.INTCON);
            if (expFIRST.contains(tokens.getNowTokenType())) { //判断有无 FuncFParams
                funcRParams = new FuncRParams(tokens);
                funcRParams.parse();
            }
            if (tokens.getNowTokenType() != TokenType.RPARENT) {
                throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            } else {
                tokens.nextToken();
            }
        } else {
            primaryExp = new PrimaryExp(tokens);
            flag = 0;
            primaryExp.parse();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (flag) {
            case 0:
                sb.append(primaryExp);
                break;
            case 1:
                sb.append(ident);
                sb.append("LPARENT (\n");
                if (funcRParams != null) {
                    sb.append(funcRParams);
                }
                sb.append("RPARENT )\n");
                break;
            case 2:
                sb.append(unaryOp);
                sb.append(unaryExp);
        }
        sb.append("<UnaryExp>\n");
        return sb.toString();
    }
}

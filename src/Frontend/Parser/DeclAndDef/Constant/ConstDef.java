package Frontend.Parser.DeclAndDef.Constant;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.AddExp;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class ConstDef extends ASTNode {
    //常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    //包含普通变量、一维数组、二维数组共三种情况
    private Ident ident;
    private ArrayList<ConstExp> constExps;
    private ConstInitVal constInitVal;

    public ConstDef(TokensReadControl tokens) {
        super(tokens);
        constExps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        ident = new Ident(tokens.getNowToken());
        tokens.nextToken();
        while (tokens.getNowTokenType() == TokenType.LBRACK) {
            tokens.nextToken();
            ConstExp constExp = new ConstExp(tokens);
            constExp.parse();
            constExps.add(constExp);
            if (tokens.getNowTokenType() != TokenType.RBRACK) {
                throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
            } else {
                tokens.nextToken();
            }
        }
        if (tokens.getNowTokenType() != TokenType.ASSIGN) {
            printError();
        }
        tokens.nextToken();
        constInitVal = new ConstInitVal(tokens);
        constInitVal.parse();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        for(ConstExp ce : constExps){
            sb.append("LBRACK [\n");
            sb.append(ce);
            sb.append("RBRACK ]\n");
        }
        sb.append("ASSIGN =\n");
        sb.append(constInitVal);
        sb.append("<ConstDef>\n");
        return sb.toString();
    }
}

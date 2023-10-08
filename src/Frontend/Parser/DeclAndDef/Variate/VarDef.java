package Frontend.Parser.DeclAndDef.Variate;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class VarDef extends ASTNode {
    //变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
    // 包含普通变量、一维数组、二维数组定义
    private Ident ident;
    private ArrayList<ConstExp> constExps;
    private InitVal initVal;
    private int flag; //0-no initVal; 1-have initVal

    public VarDef(TokensReadControl tokens){
        super(tokens);
        constExps = new ArrayList<>();
        flag = 0;
    }

    public void parse() throws CompilerError {
        ident = new Ident(tokens.getNowToken());
        while(tokens.getNowTokenType() == TokenType.LBRACK){
            tokens.nextToken();
            ConstExp constExp = new ConstExp(tokens);
            constExp.parse();
            constExps.add(constExp);
        }
        if(tokens.getNowTokenType() != TokenType.RBRACK){
            throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() == TokenType.ASSIGN){
            flag = 1;
            initVal = new InitVal(tokens);
            initVal.parse();
        }
    }
}

package Frontend.Parser.DeclAndDef.Function;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Terminator.BType;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncFParam extends ASTNode {
    //函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    //ConstExp即为 AddExp
    // 1.普通变量2.一维 数组变量 3.二维数组变量
    private BType bType;
    private Ident ident;
    private ArrayList<ConstExp> constExps;

    public FuncFParam(TokensReadControl tokens){
        super(tokens);
        constExps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        ident = new Ident(tokens.getNowToken());
        tokens.nextToken();
        if(tokens.getNowTokenType() == TokenType.LBRACK){
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.RBRACK){
                throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
            }
            tokens.nextToken();
            while(tokens.getNowTokenType() == TokenType.LBRACK){
                tokens.nextToken();
                ConstExp constExp = new ConstExp(tokens);
                constExp.parse();
                constExps.add(constExp);
                if(tokens.getNowTokenType() != TokenType.RBRACK){
                    throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
                }
                tokens.nextToken();
            }
        }
    }
}

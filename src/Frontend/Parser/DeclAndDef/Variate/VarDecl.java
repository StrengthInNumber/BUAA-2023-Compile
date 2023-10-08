package Frontend.Parser.DeclAndDef.Variate;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.BType;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class VarDecl extends ASTNode {
    //变量声明 VarDecl → BType VarDef { ',' VarDef } ';'
    // 1.花括号内重复0次 2.花括号内重复 多次
    private BType bType;
    private ArrayList<VarDef> varDefs;
    public VarDecl(TokensReadControl tokens){
        super(tokens);
        varDefs = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        VarDef varDef = new VarDef(tokens);
        varDef.parse();
        varDefs.add(varDef);
        while(tokens.getNowTokenType() == TokenType.COMMA){
            tokens.nextToken();
            varDef = new VarDef(tokens);
            varDef.parse();
            varDefs.add(varDef);
        }
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
    }
}

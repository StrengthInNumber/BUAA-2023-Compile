package Frontend.Parser.DeclAndDef;

import Check.CompilerError;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.DeclAndDef.Constant.ConstDecl;
import Frontend.Parser.DeclAndDef.Variate.VarDecl;
import Frontend.TokensReadControl;

public class Decl extends ASTNode {
    //声明 Decl → ConstDecl | VarDecl // 覆盖两种声明
    private ConstDecl constDecl;
    private VarDecl varDecl;
    private int flag; //0-const 1-var
    public Decl(TokensReadControl tokens){
        super(tokens);
    }

    public void parse() throws CompilerError {
        TokenType t = tokens.getNowTokenType();
        if(t == TokenType.CONSTTK){
            constDecl = new ConstDecl(tokens);
            flag = 0;
            constDecl.parse();
        } else if (t == TokenType.INTTK) {
            varDecl = new VarDecl(tokens);
            flag = 1;
            varDecl.parse();
        } else {
            printError();
        }
    }
}

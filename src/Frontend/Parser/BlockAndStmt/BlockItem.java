package Frontend.Parser.BlockAndStmt;

import Check.CompilerError;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.DeclAndDef.Decl;
import Frontend.TokensReadControl;

public class BlockItem extends ASTNode {
    //语句块项 BlockItem → Decl | Stmt
    // 覆盖两种语句块项
    private Decl decl;
    private Stmt stmt;
    private int flag; //0-decl 1-stmt
    public BlockItem(TokensReadControl tokens){
        super(tokens);
        flag = 0;
    }

    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() == TokenType.INTTK
                || tokens.getNowTokenType() == TokenType.CONSTTK){
            decl = new Decl(tokens);
            decl.parse();
            flag = 0;
        } else {
            stmt = new Stmt(tokens);
            stmt.parse();
            flag = 1;
        }
    }
}

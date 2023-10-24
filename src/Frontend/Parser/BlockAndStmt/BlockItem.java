package Frontend.Parser.BlockAndStmt;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
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

    public void checkError(SymbolTable table){
        if(flag == 0){
            decl.checkError(table);
        } else {
            stmt.checkError(table);
        }
    }

    public boolean isReturnStmt(){
        if(stmt == null){
            return false;
        } else {
            return stmt.isReturnStmt();
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(flag == 0){
            sb.append(decl);
        } else {
            sb.append(stmt);
        }
        return sb.toString();
    }
}

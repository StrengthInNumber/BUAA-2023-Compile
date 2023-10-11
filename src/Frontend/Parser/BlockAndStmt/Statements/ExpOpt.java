package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

import java.util.HashSet;

public class ExpOpt extends ASTNode implements StmtOpt {
    //ExpsStmt → [Exp] ';'
    // 有无Exp两种情况
    private Exp exp;

    public ExpOpt(TokensReadControl tokens) {
        super(tokens);
        exp = null;
    }

    public void parse() throws CompilerError {
        HashSet<TokenType> expFIRST = new HashSet<>();
        expFIRST.add(TokenType.PLUS);
        expFIRST.add(TokenType.MINU);
        expFIRST.add(TokenType.NOT);
        expFIRST.add(TokenType.LPARENT);
        expFIRST.add(TokenType.IDENFR);
        expFIRST.add(TokenType.INTCON);
        if (expFIRST.contains(tokens.getNowTokenType())) {
            exp = new Exp(tokens);
            exp.parse();
        }
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(exp != null){
            sb.append(exp);
        }
        sb.append("SEMICN ;\n");
        return sb.toString();
    }
}

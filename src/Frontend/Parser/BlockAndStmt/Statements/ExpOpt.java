package Frontend.Parser.BlockAndStmt.Statements;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.Symbol.SymbolTable;
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
            //throw new CompilerError(ErrorType.MISS_SEMICN, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_SEMICN));
        } else {
            tokens.nextToken();
        }
    }

    public void checkError(SymbolTable table){
        if(exp != null){
            exp.checkError(table);
        }
    }

    public void generateIR(SymbolTable table) {
        if(exp != null) {
            exp.generateIR(table);
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

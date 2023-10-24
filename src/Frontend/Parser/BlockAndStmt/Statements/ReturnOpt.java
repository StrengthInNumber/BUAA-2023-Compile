package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

import java.util.HashSet;

public class ReturnOpt extends ASTNode implements StmtOpt {
    //ReturnStmt → 'return' [Exp] ';'
    // 1.有Exp 2.无Exp
    private Exp exp;
    private int lineNum;

    public ReturnOpt(TokensReadControl tokens) {
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
        if (tokens.getNowTokenType() != TokenType.RETURNTK) {
            printError();
        }
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if(expFIRST.contains(tokens.getNowTokenType())) {
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
        if(table.isVoidFunc() && exp != null){
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.VOID_RETURN_VALUE));
        }
        table.setHasReturned(true);
        if (exp != null) {
            exp.checkError(table);
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("RETURNTK return\n");
        if(exp != null){
            sb.append(exp);
        }
        sb.append("SEMICN ;\n");
        return sb.toString();
    }
}

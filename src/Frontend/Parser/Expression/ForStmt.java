package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class ForStmt extends ASTNode {
    private LVal lVal;
    private Exp exp;
    private int lineNum;

    public ForStmt(TokensReadControl tokens) {
        super(tokens);
        lVal = new LVal(tokens);
        exp = new Exp(tokens);
    }

    public void parse() throws CompilerError {
        lVal.parse();
        if (tokens.getNowTokenType() != TokenType.ASSIGN) {
            printError();
        }
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        exp.parse();
    }

    public void checkError(SymbolTable table) {
        if (lVal.isConst(table)) {
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.CHANGE_CONST_VALUE));
        }
        lVal.checkError(table);
        exp.checkError(table);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lVal);
        sb.append("ASSIGN =\n");
        sb.append(exp);
        sb.append("<ForStmt>\n");
        return sb.toString();
    }
}

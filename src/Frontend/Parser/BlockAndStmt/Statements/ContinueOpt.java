package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class ContinueOpt extends ASTNode implements StmtOpt {
    //ContinueStmt → "continue" ";"
    private Token continueTK;
    private int lineNum;

    public ContinueOpt(TokensReadControl tokens) {
        super(tokens);
    }

    public void parse() throws CompilerError {
        if (tokens.getNowTokenType() != TokenType.CONTINUETK) {
            printError();
        }
        continueTK = tokens.getNowToken();
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            //throw new CompilerError(ErrorType.MISS_SEMICN, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_SEMICN));
        } else {
            tokens.nextToken();
        }
    }

    public void checkError(SymbolTable table) {
        if (!table.isInCircle()) {
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.NON_LOOP_STMT));
        }
    }

    public String toString() {
        return "CONTINUETK continue\n" + "SEMICN ;\n";
    }
}

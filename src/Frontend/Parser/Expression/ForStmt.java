package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrStore;
import Middle.Symbol.SymbolTable;
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

    public void generateIR(SymbolTable table) {
        IRValue lv = lVal.generateIRForAssign(table);
        IRValue ex = exp.generateIR(table);
        new IRInstrStore(ex, lv, true);
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

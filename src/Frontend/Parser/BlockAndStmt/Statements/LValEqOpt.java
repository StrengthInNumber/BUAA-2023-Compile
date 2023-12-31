package Frontend.Parser.BlockAndStmt.Statements;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IOInstr.IRInstrGetint;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrStore;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.LVal;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

public class LValEqOpt extends ASTNode implements StmtOpt {
    private LVal lVal;
    private Exp exp;
    private int flag; //0- LVal '=' Exp ';'      1- LVal '=' 'getint''('')'';'
    private int lineNum;
    public LValEqOpt(TokensReadControl tokens){
        super(tokens);
        lVal = new LVal(tokens);
    }

    public void parse() throws CompilerError {
        lVal.parse();
        lineNum = tokens.getNowTokenLineNum();
        if(tokens.getNowTokenType() != TokenType.ASSIGN){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() == TokenType.GETINTTK){
            flag = 1;
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.LPARENT){
                printError();
            }
            tokens.nextToken();
            if(tokens.getNowTokenType() != TokenType.RPARENT){
                //throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
                ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RPARENT));

            } else {
                tokens.nextToken();
            }
        } else {
            flag = 0;
            exp = new Exp(tokens);
            exp.parse();
        }
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            //throw new CompilerError(ErrorType.MISS_SEMICN, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_SEMICN));
        } else {
            tokens.nextToken();
        }
    }

    @Override
    public void checkError(SymbolTable table) {
        if(lVal.isConst(table)){
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.CHANGE_CONST_VALUE));
        }
        lVal.checkError(table);
        if(flag == 0){
            exp.checkError(table);
        }
    }

    public void generateIR(SymbolTable table) {
        IRValue lv = lVal.generateIRForAssign(table);
        if(flag == 0) {
            IRValue ex = exp.generateIR(table);
            new IRInstrStore(ex, lv, true);
        } else { //getint
            IRValue getint = new IRInstrGetint(IRBuilder.getInstance().getLocalVarName());
            new IRInstrStore(getint, lv, true);
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(lVal);
        sb.append("ASSIGN =\n");
        if(flag == 0){
            sb.append(exp);
        } else {
            sb.append("GETINTTK getint\n");
            sb.append("LPARENT (\n");
            sb.append("RPARENT )\n");
        }
        sb.append("SEMICN ;\n");
        return sb.toString();
    }
}

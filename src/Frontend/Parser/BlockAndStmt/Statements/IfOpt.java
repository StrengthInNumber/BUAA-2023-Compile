package Frontend.Parser.BlockAndStmt.Statements;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.Instruction.IRInstrJump;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Stmt;
import Frontend.Parser.Expression.Cond;
import Frontend.TokensReadControl;

public class IfOpt extends ASTNode implements StmtOpt{
    //IfStmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    // 1.有else 2.无else
    private Cond cond;
    private Stmt stmt;
    private Stmt elseStmt;
    private int flag; //0-no else  1-have else
    public IfOpt(TokensReadControl tokens){
        super(tokens);
        flag = 0;
        cond = new Cond(tokens);
        stmt = new Stmt(tokens);
    }

    public void parse() throws CompilerError{
        if(tokens.getNowTokenType() != TokenType.IFTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        cond.parse();
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            //throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RPARENT));
        } else {
            tokens.nextToken();
        }
        stmt.parse();
        if(tokens.getNowTokenType() == TokenType.ELSETK){
            tokens.nextToken();
            flag = 1;
            elseStmt = new Stmt(tokens);
            elseStmt.parse();
        }
    }

    @Override
    public void checkError(SymbolTable table) {
        cond.checkError(table);
        stmt.checkError(table);
        if(flag == 1){
            elseStmt.checkError(table);
        }
    }

    public void generateIR(SymbolTable table) {
        IRBasicBlock stmtBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
        IRBasicBlock afterIfBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
        if(flag == 0) { //no else
            cond.generateIR(table, stmtBB, afterIfBB);
            IRBuilder.getInstance().setCurBasicBlock(stmtBB);
            stmt.generateIR(table);
            new IRInstrJump(afterIfBB, true);
            IRBuilder.getInstance().setCurBasicBlock(afterIfBB);
        } else { //have else
            IRBasicBlock elseStmtBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
            cond.generateIR(table, stmtBB, elseStmtBB);
            IRBuilder.getInstance().setCurBasicBlock(stmtBB);
            stmt.generateIR(table);
            new IRInstrJump(afterIfBB, true);
            IRBuilder.getInstance().setCurBasicBlock(elseStmtBB);
            elseStmt.generateIR(table);
            new IRInstrJump(afterIfBB, true);
            IRBuilder.getInstance().setCurBasicBlock(afterIfBB);
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("IFTK if\n");
        sb.append("LPARENT (\n");
        sb.append(cond);
        sb.append("RPARENT )\n");
        sb.append(stmt);
        if(flag == 1){
            sb.append("ELSETK else\n");
            sb.append(elseStmt);
        }
        return sb.toString();
    }
}

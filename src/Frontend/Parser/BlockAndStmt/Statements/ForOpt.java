package Frontend.Parser.BlockAndStmt.Statements;

import Middle.CompilerError;
import Middle.Error.ErrorType;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.Instruction.IRInstrJump;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Stmt;
import Frontend.Parser.Expression.Cond;
import Frontend.Parser.Expression.ForStmt;
import Frontend.TokensReadControl;

public class ForOpt extends ASTNode implements StmtOpt{
    //ForOpt → 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    // 1. 无缺省 2. 缺省第一个 ForStmt 3. 缺省Cond 4. 缺省第二个ForStmt
    public ForStmt forStmt1;
    public Cond cond;
    public ForStmt forStmt2;
    public Stmt stmt;
    public ForOpt(TokensReadControl tokens){
        super(tokens);
        forStmt1 = null;
        forStmt2 = null;
        cond = null;
        stmt = new Stmt(tokens);
    }

    public void parse() throws CompilerError{
        if(tokens.getNowTokenType() != TokenType.FORTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            forStmt1 = new ForStmt(tokens);
            forStmt1.parse();
        }
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICN, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            cond = new Cond(tokens);
            cond.parse();
        }
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICN, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            forStmt2 = new ForStmt(tokens);
            forStmt2.parse();
        }
        if (tokens.getNowTokenType() != TokenType.RPARENT) {
            throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
        stmt.parse();
    }

    @Override
    public void checkError(SymbolTable table) {
        if(forStmt1 != null){
            forStmt1.checkError(table);
        }
        if(cond != null){
            cond.checkError(table);
        }
        if(forStmt2 != null){
            forStmt2.checkError(table);
        }
        table.getInCircle();
        stmt.checkError(table);
        table.getOutCircle();
    }

    public void generateIR(SymbolTable table) {
        if(forStmt1 != null) {
            forStmt1.generateIR(table);
        }
        IRBasicBlock condBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
        IRBasicBlock stmtBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
        IRBasicBlock forStmt2BB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
        IRBasicBlock afterForBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
        IRBuilder.getInstance().enterLoop(forStmt2BB, afterForBB);
        new IRInstrJump(condBB, true);
        IRBuilder.getInstance().setCurBasicBlock(condBB);
        if(cond != null) {
            cond.generateIR(table, stmtBB, afterForBB);
        } else {
            new IRInstrJump(stmtBB, true);
        }
        IRBuilder.getInstance().setCurBasicBlock(stmtBB);
        stmt.generateIR(table);
        new IRInstrJump(forStmt2BB, true);
        IRBuilder.getInstance().setCurBasicBlock(forStmt2BB);
        if(forStmt2 != null) {
            forStmt2.generateIR(table);
        }
        new IRInstrJump(condBB, true);
        IRBuilder.getInstance().leaveLoop();
        IRBuilder.getInstance().setCurBasicBlock(afterForBB);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("FORTK for\n");
        sb.append("LPARENT (\n");
        if(forStmt1 != null){
            sb.append(forStmt1);
        }
        sb.append("SEMICN ;\n");
        if(cond != null){
            sb.append(cond);
        }
        sb.append("SEMICN ;\n");
        if(forStmt2 != null){
            sb.append(forStmt2);
        }
        sb.append("RPARENT )\n");
        sb.append(stmt);
        return sb.toString();
    }
}

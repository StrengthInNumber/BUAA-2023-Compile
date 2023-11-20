package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.Symbol.SymbolTable;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class Cond extends ASTNode {
    //条件表达式 Cond → LOrExp
    private LOrExp lOrExp;
    public Cond(TokensReadControl tokens){
        super(tokens);
        lOrExp = new LOrExp(tokens);
    }

    public void parse() throws CompilerError {
        lOrExp.parse();
    }

    public void generateIR(SymbolTable table, IRBasicBlock trueBB, IRBasicBlock falseBB) {
        lOrExp.generateIR(table, trueBB, falseBB);
    }
    public void checkError(SymbolTable table){
        lOrExp.checkError(table);
    }
    public String toString(){
        return lOrExp.toString() + "<Cond>\n";
    }
}

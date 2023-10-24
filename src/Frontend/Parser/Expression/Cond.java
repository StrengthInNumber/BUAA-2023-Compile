package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
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

    public void checkError(SymbolTable table){
        lOrExp.checkError(table);
    }
    public String toString(){
        return lOrExp.toString() + "<Cond>\n";
    }
}

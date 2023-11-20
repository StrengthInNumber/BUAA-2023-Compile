package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.IRValue;
import Middle.Symbol.SymbolTable;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class Exp extends ASTNode {
    private AddExp addExp;
    public Exp(TokensReadControl tokens){
        super(tokens);
        addExp = new AddExp(tokens);
    }

    public void parse() throws CompilerError {
        addExp.parse();
    }

    public void checkError(SymbolTable table){
        addExp.checkError(table);
    }
    public int getDim(SymbolTable table){
        return addExp.getDim(table);
    }
    public int getConstValue(SymbolTable table){
        return addExp.getConstValue(table);
    }
    public IRValue generateIR(SymbolTable table) {
        return addExp.generateIR(table);
    }
    public String toString(){
        return addExp.toString() + "<Exp>\n";
    }
}

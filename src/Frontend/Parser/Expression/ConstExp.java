package Frontend.Parser.Expression;

import Check.CompilerError;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

public class ConstExp extends ASTNode {
    private AddExp addExp;
    public ConstExp(TokensReadControl tokens){
        super(tokens);
        addExp = new AddExp(tokens);
    }

    public void parse() throws CompilerError {
        addExp.parse();
    }

    public String toString(){
        return addExp.toString() + "<ConstExp>\n";
    }
}

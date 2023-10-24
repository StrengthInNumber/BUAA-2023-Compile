package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class LOrExp extends ASTNode {
    private ArrayList<LAndExp> lAndExps;
    public LOrExp(TokensReadControl tokens){
        super(tokens);
        lAndExps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        LAndExp lAndExp = new LAndExp(tokens);
        lAndExp.parse();
        lAndExps.add(lAndExp);
        while(tokens.getNowTokenType() == TokenType.OR){
            tokens.nextToken();
            lAndExp = new LAndExp(tokens);
            lAndExp.parse();
            lAndExps.add(lAndExp);
        }
    }

    public void checkError(SymbolTable table){
        for(LAndExp e : lAndExps){
            e.checkError(table);
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lAndExps.get(0));
        sb.append("<LOrExp>\n");
        for (int i = 1; i < lAndExps.size(); i++) {
            sb.append("OR ||\n");
            sb.append(lAndExps.get(i));
            sb.append("<LOrExp>\n");
        }
        return sb.toString();
    }
}

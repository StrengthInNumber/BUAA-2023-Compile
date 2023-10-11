package Frontend.Parser.Expression;

import Check.CompilerError;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class LAndExp extends ASTNode {
    private final ArrayList<EqExp> eqExps;

    public LAndExp(TokensReadControl tokens) {
        super(tokens);
        eqExps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        EqExp eqExp = new EqExp(tokens);
        eqExp.parse();
        eqExps.add(eqExp);
        while (tokens.getNowTokenType() == TokenType.AND) {
            tokens.nextToken();
            eqExp = new EqExp(tokens);
            eqExp.parse();
            eqExps.add(eqExp);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(eqExps.get(0));
        sb.append("<LAndExp>\n");
        for (int i = 1; i < eqExps.size(); i++) {
            sb.append("AND &&\n");
            sb.append(eqExps.get(i));
            sb.append("<LAndExp>\n");
        }
        return sb.toString();
    }
}

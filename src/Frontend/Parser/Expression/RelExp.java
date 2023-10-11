package Frontend.Parser.Expression;

import Check.CompilerError;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class RelExp extends ASTNode {
    private ArrayList<AddExp> addExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;
    public RelExp(TokensReadControl tokens){
        super(tokens);
        addExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.GRE);
        opts.add(TokenType.LSS);
        opts.add(TokenType.GEQ);
        opts.add(TokenType.LEQ);
    }

    public void parse() throws CompilerError {
        AddExp addExp = new AddExp(tokens);
        addExp.parse();
        addExps.add(addExp);
        optList.add(null);
        while(opts.contains(tokens.getNowTokenType())){
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            addExp = new AddExp(tokens);
            addExp.parse();
            addExps.add(addExp);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(addExps.get(0));
        sb.append("<RelExp>\n");
        for (int i = 1; i < addExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(addExps.get(i));
            sb.append("<RelExp>\n");
        }
        return sb.toString();
    }
}

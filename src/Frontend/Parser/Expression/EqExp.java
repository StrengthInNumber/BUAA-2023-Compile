package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class EqExp extends ASTNode {
    private ArrayList<RelExp> relExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;
    public EqExp(TokensReadControl tokens){
        super(tokens);
        relExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.EQL);
        opts.add(TokenType.NEQ);
    }

    public void parse() throws CompilerError {
        RelExp relExp = new RelExp(tokens);
        relExp.parse();
        relExps.add(relExp);
        optList.add(null);
        while(opts.contains(tokens.getNowTokenType())){
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            relExp = new RelExp(tokens);
            relExp.parse();
            relExps.add(relExp);
        }
    }

    public void checkError(SymbolTable table){
        for(RelExp e : relExps){
            e.checkError(table);
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(relExps.get(0));
        sb.append("<EqExp>\n");
        for (int i = 1; i < relExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(relExps.get(i));
            sb.append("<EqExp>\n");
        }
        return sb.toString();
    }
}

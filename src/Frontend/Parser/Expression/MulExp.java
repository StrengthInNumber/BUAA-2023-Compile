package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class MulExp extends ASTNode {

    private ArrayList<UnaryExp> unaryExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;
    public MulExp(TokensReadControl tokens){
        super(tokens);
        unaryExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.MULT);
        opts.add(TokenType.DIV);
        opts.add(TokenType.MOD);
    }

    public void parse() throws CompilerError {
        UnaryExp unaryExp = new UnaryExp(tokens);
        unaryExp.parse();
        unaryExps.add(unaryExp);
        optList.add(null);
        while(opts.contains(tokens.getNowTokenType())){
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            unaryExp = new UnaryExp(tokens);
            unaryExp.parse();
            unaryExps.add(unaryExp);
        }
    }
    public void checkError(SymbolTable table){
        for(UnaryExp m : unaryExps){
            m.checkError(table);
        }
    }

    public int getDim(SymbolTable table){
        return unaryExps.get(0).getDim(table);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(unaryExps.get(0));
        sb.append("<MulExp>\n");
        for (int i = 1; i < unaryExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(unaryExps.get(i));
            sb.append("<MulExp>\n");
        }
        return sb.toString();
    }
}

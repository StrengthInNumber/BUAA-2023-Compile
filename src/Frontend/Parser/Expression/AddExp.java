package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class AddExp extends ASTNode {
    private ArrayList<MulExp> mulExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;

    public AddExp(TokensReadControl tokens) {
        super(tokens);
        mulExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.PLUS);
        opts.add(TokenType.MINU);
    }

    public void parse() throws CompilerError {
        MulExp mulExp = new MulExp(tokens);
        mulExp.parse();
        mulExps.add(mulExp);
        optList.add(null);
        while (opts.contains(tokens.getNowTokenType())) {
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            mulExp = new MulExp(tokens);
            mulExp.parse();
            mulExps.add(mulExp);
        }
    }

    public void checkError(SymbolTable table){
        for(MulExp m : mulExps){
            m.checkError(table);
        }
    }

    public int getDim(SymbolTable table){
        return mulExps.get(0).getDim(table);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mulExps.get(0));
        sb.append("<AddExp>\n");
        for (int i = 1; i < mulExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(mulExps.get(i));
            sb.append("<AddExp>\n");
        }
        return sb.toString();
    }
}

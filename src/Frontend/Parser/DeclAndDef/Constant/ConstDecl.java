package Frontend.Parser.DeclAndDef.Constant;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.BType;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class ConstDecl extends ASTNode {
    //常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
    // 1.花括号内重复0 次 2.花括号内重复多次
    private Token constTk;
    private BType bType;
    private final ConstDef constDef;
    private final ArrayList<ConstDef> constDefs;

    public ConstDecl(TokensReadControl tokens) {
        super(tokens);
        constDef = new ConstDef(tokens);
        constDefs = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        TokenType tt = tokens.getNowTokenType();
        if (tt != TokenType.CONSTTK) {
            printError();
        }
        constTk = tokens.getNowToken();
        tokens.nextToken();
        tt = tokens.getNowTokenType();
        if (tt != TokenType.INTTK) {
            printError();
        }
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        constDef.parse();
        tt = tokens.getNowTokenType();
        while (tt == TokenType.COMMA) {
            tokens.nextToken();
            ConstDef cd = new ConstDef(tokens);
            cd.parse();
            constDefs.add(cd);
            tt = tokens.getNowTokenType();
        }
        if (tt != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(constTk);
        sb.append(bType);
        sb.append(constDef);
        for (ConstDef cd : constDefs) {
            sb.append("COMMA ,\n");
            sb.append(cd);
        }
        sb.append("SEMICN ;\n");
        sb.append("<ConstDecl>\n");
        return sb.toString();
    }
}

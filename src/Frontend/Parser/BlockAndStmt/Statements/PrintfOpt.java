package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class PrintfOpt extends ASTNode implements StmtOpt{
    //PrintfStmt → 'printf''('FormatString{','Exp}')'';'
    // 1.有Exp 2.无Exp
    public Token formatString;
    public ArrayList<Exp> exps;
    public PrintfOpt(TokensReadControl tokens){
        super(tokens);
        exps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() != TokenType.PRINTFTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.STRCON){
            printError();
        }
        formatString = tokens.getNowToken();
        tokens.nextToken();
        while(tokens.getNowTokenType() == TokenType.COMMA){
            tokens.nextToken();
            Exp exp = new Exp(tokens);
            exp.parse();
            exps.add(exp);
        }
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            printError();
        }
        tokens.nextToken();
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        } else {
            tokens.nextToken();
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PRINTFTK printf\n");
        sb.append("LPARENT (\n");
        sb.append(formatString);
        for(Exp e : exps){
            sb.append("COMMA ,\n");
            sb.append(e);
        }
        sb.append("RPARENT )\n");
        sb.append("SEMICN ;\n");
        return sb.toString();
    }
}
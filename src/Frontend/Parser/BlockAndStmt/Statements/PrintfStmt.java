package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.ErrorType;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class PrintfStmt extends ASTNode implements StmtOpt{
    //PrintfStmt → 'printf''('FormatString{','Exp}')'';'
    // 1.有Exp 2.无Exp
    public Token formatString;
    public ArrayList<Exp> exps;
    public PrintfStmt(TokensReadControl tokens){
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
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            throw new CompilerError(ErrorType.MISS_SEMICOLON, tokens.getNowTokenLineNum());
        }
        tokens.nextToken();
    }
}

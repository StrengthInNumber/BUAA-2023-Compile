package Frontend.Parser;

import Middle.CompilerError;
import Frontend.TokensReadControl;

public class Parser {
    private CompUnit compUnit;
    private TokensReadControl tokens;
    public Parser(TokensReadControl tokens){
        this.tokens = tokens;
        compUnit = new CompUnit(tokens);
    }

    public void parse() throws CompilerError {
        compUnit.parse();
    }

    public CompUnit getCompUnit() {
        return compUnit;
    }

    public void checkError(){
        compUnit.checkError();
    }
    public String toString(){
        return compUnit.toString();
    }
}

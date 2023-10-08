package Frontend.Parser;

import Frontend.TokensReadControl;

public class ASTNode {
    public TokensReadControl tokens;

    public ASTNode(TokensReadControl tokens) {
        this.tokens = tokens;
    }

    protected void printError() {
        System.out.print("in line " + tokens.getNowTokenLineNum() + "wrong in parsing " + tokens.getNowToken());
    }
}

package Frontend.Parser.BlockAndStmt;

import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class Block extends ASTNode {
    //语句块 Block → '{' { BlockItem } '}'
    // 1.花括号内重复0次 2.花括号内重复多次
    private ArrayList<BlockItem> blockItems;
    public Block(TokensReadControl tokens){
        super(tokens);
        blockItems = new ArrayList<>();
    }
    public void parse(){
        if(tokens.getNowTokenType() != TokenType.LBRACE){
            printError();
        }
        while(tokens.getNowTokenType() != TokenType.RBRACE){
            BlockItem blockItem = new BlockItem(tokens);
            blockItem.parse();
            blockItems.add(blockItem);
        }
        tokens.nextToken();
    }
}

package Frontend.Parser.BlockAndStmt;

import Check.CompilerError;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Statements.StmtOpt;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class Block extends ASTNode implements StmtOpt {
    //语句块 Block → '{' { BlockItem } '}'
    // 1.花括号内重复0次 2.花括号内重复多次
    private ArrayList<BlockItem> blockItems;
    public Block(TokensReadControl tokens){
        super(tokens);
        blockItems = new ArrayList<>();
    }
    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() != TokenType.LBRACE){
            printError();
        }
        tokens.nextToken();
        while(tokens.getNowTokenType() != TokenType.RBRACE){
            BlockItem blockItem = new BlockItem(tokens);
            blockItem.parse();
            blockItems.add(blockItem);
        }
        tokens.nextToken();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("LBRACE {\n");
        for(BlockItem b : blockItems){
            sb.append(b);
        }
        sb.append("RBRACE }\n");
        sb.append("<Block>\n");
        return sb.toString();
    }
}

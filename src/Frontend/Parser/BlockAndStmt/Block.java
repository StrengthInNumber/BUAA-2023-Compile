package Frontend.Parser.BlockAndStmt;

import Middle.CompilerError;
import Middle.Symbol.SymbolTable;
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

    public void checkError(SymbolTable table){
        for(BlockItem b : blockItems) {
            b.checkError(table);
        }
    }

    public boolean hasReturnAtEnd(){
        if(blockItems.isEmpty()){
            return false;
        } else {
            return blockItems.get(blockItems.size() - 1).isReturnStmt();
        }
    }

    public void generateIR(SymbolTable table) {
        for(BlockItem b : blockItems) {
            b.generateIR(table);
        }
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

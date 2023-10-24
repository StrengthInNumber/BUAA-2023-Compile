package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.BlockItem;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class BlockOpt extends ASTNode implements StmtOpt {
    //语句块 Block → '{' { BlockItem } '}'
    // 1.花括号内重复0次 2.花括号内重复多次
    private ArrayList<BlockItem> blockItems;
    private SymbolTable symbolTable;
    public BlockOpt(TokensReadControl tokens){
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

    public void checkError(SymbolTable lastTable){
        symbolTable = new SymbolTable(lastTable);
        for(BlockItem b : blockItems) {
            b.checkError(symbolTable);
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

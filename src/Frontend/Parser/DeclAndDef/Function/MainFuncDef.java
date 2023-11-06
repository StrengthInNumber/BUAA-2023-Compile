package Frontend.Parser.DeclAndDef.Function;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolFunc;
import Check.Symbol.SymbolTable;
import Check.Symbol.ValueType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Block;
import Frontend.TokensReadControl;

public class MainFuncDef extends ASTNode {
    //主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block
    // 存在main函数
    private Block block;
    private SymbolTable symbolTable;
    private int identLineNum;
    private int endLineNum;
    public MainFuncDef(TokensReadControl tokens){
        super(tokens);
    }
    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() != TokenType.INTTK){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.MAINTK){
            printError();
        }
        identLineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            //throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RPARENT));
        }else {
            tokens.nextToken();
        }
        block = new Block(tokens);
        block.parse();
        endLineNum = tokens.getLastTokenLineNum();
    }

    public void checkError(SymbolTable lastTable){
        symbolTable = new SymbolTable(lastTable, ValueType.INT);
        lastTable.addSymbol(new SymbolFunc(identLineNum, "main", ValueType.INT,
                symbolTable.getFuncFParamType(), symbolTable.getFuncFParamDims()), false);
        block.checkError(symbolTable);
        if(!block.hasReturnAtEnd()){
            ErrorTable.getInstance().addError(new Error(endLineNum, ErrorType.MISS_RETURN));
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("INTTK int\n");
        sb.append("MAINTK main\n");
        sb.append("LPARENT (\n");
        sb.append("RPARENT )\n");
        sb.append(block);
        sb.append("<MainFuncDef>\n");
        return sb.toString();
    }
}

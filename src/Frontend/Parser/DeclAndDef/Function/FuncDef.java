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
import Frontend.Parser.Terminator.FuncType;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

public class FuncDef extends ASTNode {
    //函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    // 1.无形参 2.有形参
    private FuncType funcType;
    private Ident ident;
    private FuncFParams funcFParams;
    private int flag; //0-no params 1-have params
    private Block block;
    private int identLineNum;
    private SymbolTable symbolTable;
    private int endLineNum;
    public FuncDef(TokensReadControl tokens){
        super(tokens);
        flag = 0;
    }
    public void parse() throws CompilerError {
        funcType = new FuncType(tokens.getNowToken());
        tokens.nextToken();
        ident = new Ident(tokens.getNowToken());
        identLineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() == TokenType.INTTK){
            funcFParams = new FuncFParams(tokens);
            funcFParams.parse();
            flag = 1;
        }
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            //throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getNowTokenLineNum(), ErrorType.MISS_RPARENT));
        } else {
            tokens.nextToken();
        }
        block = new Block(tokens);
        block.parse();
        endLineNum = tokens.getLastTokenLineNum();
    }

    public void checkError(SymbolTable lastTable){
        symbolTable = new SymbolTable(lastTable, funcType.getValueType());
        if(flag != 0){
            funcFParams.checkError(symbolTable);
        }
        lastTable.addSymbol(new SymbolFunc(identLineNum, ident.getName(), funcType.getValueType(),
                symbolTable.getFuncFParamType(), symbolTable.getFuncFParamDims()), false);
        block.checkError(symbolTable);
        if(funcType.getValueType() != ValueType.VOID && !block.hasReturnAtEnd()){
            ErrorTable.getInstance().addError(new Error(endLineNum, ErrorType.MISS_RETURN));
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(funcType);
        sb.append(ident);
        sb.append("LPARENT (\n");
        if(flag == 1){
            sb.append(funcFParams);
        }
        sb.append("RPARENT )\n");
        sb.append(block);
        sb.append("<FuncDef>\n");
        return sb.toString();
    }
}

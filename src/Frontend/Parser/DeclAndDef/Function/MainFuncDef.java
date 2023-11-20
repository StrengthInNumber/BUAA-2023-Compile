package Frontend.Parser.DeclAndDef.Function;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.Function.IRParam;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRType;
import Middle.LLVMIR.Type.IRVoidType;
import Middle.Symbol.SymbolFunc;
import Middle.Symbol.SymbolTable;
import Middle.Symbol.ValueType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.BlockAndStmt.Block;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class MainFuncDef extends ASTNode {
    //主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block
    // 存在main函数
    private Block block;
    private SymbolTable symbolTable_error;
    private SymbolTable symbolTable_ir;
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
        symbolTable_error = new SymbolTable(lastTable, ValueType.INT);
        lastTable.addSymbol(new SymbolFunc(identLineNum, "main", ValueType.INT,
                symbolTable_error.getFuncFParamType(), symbolTable_error.getFuncFParamDims()), false);
        block.checkError(symbolTable_error);
        if(!block.hasReturnAtEnd()){
            ErrorTable.getInstance().addError(new Error(endLineNum, ErrorType.MISS_RETURN));
        }
    }

    public void generateIR(SymbolTable lastTable) {
        symbolTable_ir = new SymbolTable(lastTable, ValueType.INT);
        IRType returnType;
        ArrayList<IRParam> irParams = new ArrayList<>();
        returnType = IRIntegerType.INT32;
        //新建基本块（读入形参时需要相应加入 alloca 语句)
        String blockName = IRBuilder.getInstance().getBasicBlockName();
        IRBasicBlock bb = new IRBasicBlock(blockName, false);
        IRBuilder.getInstance().setCurBasicBlock(bb);
        SymbolFunc sf = new SymbolFunc(identLineNum, "main", ValueType.INT,
                symbolTable_ir.getFuncFParamType(), symbolTable_ir.getFuncFParamDims());
        IRFunction function = new IRFunction("@main", returnType, irParams);
        IRBuilder.getInstance().setCurFunction(function);
        IRBuilder.getInstance().addBasicBlock(bb);
        sf.setIRValue(function);
        lastTable.addSymbol(sf, false);
        block.generateIR(symbolTable_ir);
        IRBuilder.getInstance().addReturn(returnType);
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

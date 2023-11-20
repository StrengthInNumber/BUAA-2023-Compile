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
import Frontend.Parser.Terminator.FuncType;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncDef extends ASTNode {
    //函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    // 1.无形参 2.有形参
    private FuncType funcType;
    private Ident ident;
    private FuncFParams funcFParams;
    private int flag; //0-no params 1-have params
    private Block block;
    private int identLineNum;
    private SymbolTable symbolTable_error;
    private SymbolTable symbolTable_ir;
    private int endLineNum;

    public FuncDef(TokensReadControl tokens) {
        super(tokens);
        flag = 0;
    }

    public void parse() throws CompilerError {
        funcType = new FuncType(tokens.getNowToken());
        tokens.nextToken();
        ident = new Ident(tokens.getNowToken());
        identLineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if (tokens.getNowTokenType() != TokenType.LPARENT) {
            printError();
        }
        tokens.nextToken();
        if (tokens.getNowTokenType() == TokenType.INTTK) {
            funcFParams = new FuncFParams(tokens);
            funcFParams.parse();
            flag = 1;
        }
        if (tokens.getNowTokenType() != TokenType.RPARENT) {
            //throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RPARENT));
        } else {
            tokens.nextToken();
        }
        block = new Block(tokens);
        block.parse();
        endLineNum = tokens.getLastTokenLineNum();
    }

    public void checkError(SymbolTable lastTable) {
        symbolTable_error = new SymbolTable(lastTable, funcType.getValueType());
        if (flag != 0) {
            funcFParams.checkError(symbolTable_error);
        }
        lastTable.addSymbol(new SymbolFunc(identLineNum, ident.getName(), funcType.getValueType(),
                symbolTable_error.getFuncFParamType(), symbolTable_error.getFuncFParamDims()), false);
        block.checkError(symbolTable_error);
        if (funcType.getValueType() != ValueType.VOID && !block.hasReturnAtEnd()) {
            ErrorTable.getInstance().addError(new Error(endLineNum, ErrorType.MISS_RETURN));
        }
    }

    public void generateIR(SymbolTable lastTable) {
        //新建本函数符号表
        symbolTable_ir = new SymbolTable(lastTable, funcType.getValueType());
        IRType returnType;
        ArrayList<IRParam> irParams = new ArrayList<>();
        //判断函数返回值类型
        if (funcType.getValueType() == ValueType.INT) {
            returnType = IRIntegerType.INT32;
        } else {
            returnType = IRVoidType.VOID;
        }
        //新建基本块（读入形参时需要相应加入 alloca 语句)
        String blockName = IRBuilder.getInstance().getBasicBlockName();
        IRBasicBlock bb = new IRBasicBlock(blockName, false);
        IRBuilder.getInstance().setCurBasicBlock(bb);
        if (flag != 0) {
            irParams = funcFParams.generateIR(symbolTable_ir, bb);
        }
        SymbolFunc sf = new SymbolFunc(identLineNum, ident.getName(), funcType.getValueType(),
                symbolTable_ir.getFuncFParamType(), symbolTable_ir.getFuncFParamDims());
        IRFunction function = new IRFunction(IRBuilder.getInstance().getFunctionName(ident.getName()),
                returnType, irParams);
        IRBuilder.getInstance().setCurFunction(function);
        IRBuilder.getInstance().addBasicBlock(bb);
        sf.setIRValue(function);
        lastTable.addSymbol(sf, false);
        block.generateIR(symbolTable_ir);
        IRBuilder.getInstance().addReturn(returnType);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(funcType);
        sb.append(ident);
        sb.append("LPARENT (\n");
        if (flag == 1) {
            sb.append(funcFParams);
        }
        sb.append("RPARENT )\n");
        sb.append(block);
        sb.append("<FuncDef>\n");
        return sb.toString();
    }
}

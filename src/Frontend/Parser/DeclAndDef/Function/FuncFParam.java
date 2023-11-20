package Frontend.Parser.DeclAndDef.Function;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.Function.IRParam;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrAlloca;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrStore;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.LLVMIR.Type.IRPointerType;
import Middle.LLVMIR.Type.IRType;
import Middle.Symbol.SymbolTable;
import Middle.Symbol.SymbolVar;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Terminator.BType;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncFParam extends ASTNode {
    //函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    //ConstExp即为 AddExp
    // 1.普通变量2.一维 数组变量 3.二维数组变量
    private BType bType;
    private Ident ident;
    private ArrayList<ConstExp> constExps;
    private int flag; //0-普通变量 1-一维数组 2-二维数组
    private int lineNum;

    public FuncFParam(TokensReadControl tokens) {
        super(tokens);
        constExps = new ArrayList<>();
        flag = 0;
    }

    public void parse() throws CompilerError {
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        ident = new Ident(tokens.getNowToken());
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if (tokens.getNowTokenType() == TokenType.LBRACK) {
            flag = 1;
            tokens.nextToken();
            if (tokens.getNowTokenType() != TokenType.RBRACK) {
                //throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
                ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RBRACK));
            } else {
                tokens.nextToken();
            }
            while (tokens.getNowTokenType() == TokenType.LBRACK) {
                flag = 2;
                tokens.nextToken();
                ConstExp constExp = new ConstExp(tokens);
                constExp.parse();
                constExps.add(constExp);
                if (tokens.getNowTokenType() != TokenType.RBRACK) {
                    //throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
                    ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RBRACK));
                } else {
                    tokens.nextToken();
                }
            }
        }
    }

    public void checkError(SymbolTable table) {
        int dim;
        if(flag == 0){
            dim = 0;
        } else {
            dim = constExps.size() + 1;
        }
        table.addSymbol(new SymbolVar(lineNum, ident.getName(), dim, bType.getValueType()), true);
    }

    public IRParam generateIR(SymbolTable table, IRBasicBlock bb) {
        IRType type;
        IRParam param;
        SymbolVar sv;
        int dim;
        ArrayList<Integer> lengths = new ArrayList<>();
        lengths.add(0);
        for(ConstExp ce : constExps) {
            lengths.add(ce.getConstValue(table));
        }
        if(flag == 0){
            dim = 0;
        } else {
            dim = constExps.size() + 1;
        }
        if(flag == 0) {
            type = IRIntegerType.INT32;
            IRInstrAlloca ia = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(), type, true);
            param = new IRParam(IRBuilder.getInstance().getFParamName(), type);
            new IRInstrStore(param, ia, true);
            sv = new SymbolVar(lineNum, ident.getName(), dim, bType.getValueType(), lengths);
            sv.setIRValue(ia);
        } else {
            type = new IRPointerType(IRIntegerType.INT32);
            param = new IRParam(IRBuilder.getInstance().getFParamName(), type);
            sv = new SymbolVar(lineNum, ident.getName(), dim, bType.getValueType(), lengths);
            sv.setIRValue(param);
        }
        table.addSymbol(sv, true);
        return param;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType);
        sb.append(ident);
        if (flag != 0) {
            sb.append("LBRACK [\n");
            sb.append("RBRACK ]\n");
            for (ConstExp ce : constExps) {
                sb.append("LBRACK [\n");
                sb.append(ce);
                sb.append("RBRACK ]\n");
            }
        }
        sb.append("<FuncFParam>\n");
        return sb.toString();
    }
}

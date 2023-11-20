package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.Function.IRFunction;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.*;
import Middle.LLVMIR.Instruction.FunctionInstr.IRInstrCall;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.Symbol.SymbolFunc;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.Ident;
import Frontend.Parser.Terminator.UnaryOp;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class UnaryExp extends ASTNode {
    //一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    // 3种情况均需覆盖,函 数调用也需要覆盖FuncRParams的不同情况
    private int flag; //0-primaryExp 1-Ident[][] 2-Unary
    private PrimaryExp primaryExp;
    private Ident ident;
    private FuncRParams funcRParams;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;
    private int lineNum;

    public UnaryExp(TokensReadControl tokens) {
        super(tokens);
        funcRParams = null;
        flag = 0;
    }

    public void parse() throws CompilerError {
        TokenType t1 = tokens.getNowTokenType();
        TokenType t2 = tokens.getPreTokenType(1);
        if (t1 == TokenType.PLUS || t1 == TokenType.MINU || t1 == TokenType.NOT) {
            unaryOp = new UnaryOp(tokens.getNowToken());
            flag = 2;
            tokens.nextToken();
            unaryExp = new UnaryExp(tokens);
            unaryExp.parse();
        } else if (t1 == TokenType.IDENFR && t2 == TokenType.LPARENT) {
            ident = new Ident(tokens.getNowToken());
            lineNum = tokens.getNowTokenLineNum();
            flag = 1;
            tokens.nextToken();
            tokens.nextToken();
            HashSet<TokenType> expFIRST = new HashSet<>();
            expFIRST.add(TokenType.PLUS);
            expFIRST.add(TokenType.MINU);
            expFIRST.add(TokenType.NOT);
            expFIRST.add(TokenType.LPARENT);
            expFIRST.add(TokenType.IDENFR);
            expFIRST.add(TokenType.INTCON);
            if (expFIRST.contains(tokens.getNowTokenType())) { //判断有无 FuncFParams
                funcRParams = new FuncRParams(tokens);
                funcRParams.parse();
            }
            if (tokens.getNowTokenType() != TokenType.RPARENT) {
                //throw new CompilerError(ErrorType.MISS_RPARENT, tokens.getNowTokenLineNum());
                ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RPARENT));
            } else {
                tokens.nextToken();
            }
        } else {
            primaryExp = new PrimaryExp(tokens);
            flag = 0;
            primaryExp.parse();
        }
    }

    public void checkError(SymbolTable table){
        switch (flag) {
            case 0:
                primaryExp.checkError(table);
                break;
            case 1:
                if(!table.haveSymbol(ident.getName(), true)){
                    ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.UNDEFINED_SYMBOL));
                } else {
                    if (funcRParams == null) {
                        if(table.getFuncParamSize(ident.getName()) != 0) {
                            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.PARAM_NUM_MISMATCH));
                        }
                    } else {
                        funcRParams.checkError(table, ident.getName());
                    }
                }
                break;
            case 2:
                unaryExp.checkError(table);
                break;
        }
    }
    public int getDim(SymbolTable table){
        return switch (flag) {
            case 0 -> primaryExp.getDim(table);
            case 1 -> table.getFuncDim(ident.getName());
            case 2 -> unaryExp.getDim(table);
            default -> {
                System.out.println("wrong in UnaryExp.getDim");
                yield -1;
            }
        };
    }

    public IRValue generateIR(SymbolTable table) {
        IRValue v0 = new IRConstInt(IRIntegerType.INT32, 0);
        switch (flag) {
            case 0:
                return primaryExp.generateIR(table);
            case 1:
                SymbolFunc sf = (SymbolFunc) table.getSymbol(ident.getName());
                IRFunction irF = (IRFunction) sf.getIRValue();
                if(funcRParams == null) {
                    ArrayList<IRValue> emp = new ArrayList<>();
                    return new IRInstrCall(IRBuilder.getInstance().getLocalVarName(), irF,
                            emp, true);
                } else {
                    return new IRInstrCall(IRBuilder.getInstance().getLocalVarName(), irF,
                            funcRParams.generateIR(table), true);
                }
            case 2:
                IRValue vu = unaryExp.generateIR(table);
                if (unaryOp.isPlus()) {
                    return vu;
                } else if (unaryOp.isMinus()) {
                    return new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(),
                            IRInstrType.SUB, true, v0, vu);
                } else { // !
                    IRInstr instr = new IRInstrIcmp(IRBuilder.getInstance().getLocalVarName(),
                            IRInstrType.EQ, true, v0, vu);
                    return new IRInstrZext(IRBuilder.getInstance().getLocalVarName(), true,
                            instr, IRIntegerType.INT32);
                }
            default:
                System.out.println("wrong in UnaryExp.generateIR");
                return null;
        }
    }
    public int getConstValue(SymbolTable table){
        switch (flag) {
            case 0:
                return primaryExp.getConstValue(table);
            case 1:
                System.out.println("wrong in UnaryExp.getConstValue: get a function");
                return -9999;
            case 2:
                int ans = unaryExp.getConstValue(table);
                if(unaryOp.isPlus()) {
                    return ans;
                } else if (unaryOp.isMinus()){
                    return -ans;
                } else {
                    System.out.println("wrong in UnaryExp.getConstValue: get a \"!\" unaryOP");
                    return -9999;
                }
            default:
                System.out.println("wrong in UnaryExp.getConstValue");
                return -9999;
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (flag) {
            case 0:
                sb.append(primaryExp);
                break;
            case 1:
                sb.append(ident);
                sb.append("LPARENT (\n");
                if (funcRParams != null) {
                    sb.append(funcRParams);
                }
                sb.append("RPARENT )\n");
                break;
            case 2:
                sb.append(unaryOp);
                sb.append(unaryExp);
        }
        sb.append("<UnaryExp>\n");
        return sb.toString();
    }
}

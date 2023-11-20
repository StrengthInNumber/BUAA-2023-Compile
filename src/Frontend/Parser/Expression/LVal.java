package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.*;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrLoad;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.Symbol.Symbol;
import Middle.Symbol.SymbolConst;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;
import Middle.Symbol.SymbolVar;

import java.util.ArrayList;

public class LVal extends ASTNode {
    //左值表达式 LVal → Ident {'[' Exp ']'}
    // 1.普通变量 2.一维数组 3.二维数组
    private Ident ident;
    private ArrayList<Exp> exps;
    private int lineNum;
    public LVal(TokensReadControl tokens){
        super(tokens);
        exps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        ident = new Ident(tokens.getNowToken());
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        while(tokens.getNowTokenType() == TokenType.LBRACK){
            tokens.nextToken();
            Exp exp = new Exp(tokens);
            exp.parse();
            exps.add(exp);
            if(tokens.getNowTokenType() != TokenType.RBRACK){
                //throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
                ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RBRACK));
            } else {
                tokens.nextToken();
            }
        }
    }

    public boolean isConst(SymbolTable table){
        return table.getSymbol(ident.getName()) instanceof SymbolConst;
    }

    public void checkError(SymbolTable table){
        if(!table.haveSymbol(ident.getName(), false)){
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.UNDEFINED_SYMBOL));
        }
        for(Exp e : exps){
            e.checkError(table);
        }
    }
    public int getDim(SymbolTable table){
        return table.getSymbol(ident.getName()).getDim() - exps.size();
    }

    public int getConstValue(SymbolTable table) {
        return switch (exps.size()) {
            case 0 -> table.getConstValue_0(ident.getName());
            case 1 -> table.getConstValue_1(ident.getName(), exps.get(0).getConstValue(table));
            case 2 -> table.getConstValue_2(ident.getName(), exps.get(0).getConstValue(table), exps.get(1).getConstValue(table));
            default -> {
                System.out.println("wrong in LVal.getConstValue: dimension of array is larger than 2");
                yield -9999;
            }
        };
    }

    public IRValue generateIRForValue(SymbolTable table) {
        Symbol symbol = table.getSymbol(ident.getName());
        IRValue iv = symbol.getIRValue();
        int requireDim = exps.size();
        IRInstr instr;
        switch (symbol.getDim()) {
            case 0:
                return new IRInstrLoad(IRBuilder.getInstance().getLocalVarName(), iv, true);
            case 1:
                if(requireDim == 0) { //需要返回一维数组指针
                    return new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(),
                            symbol.getIRValue(), new IRConstInt(IRIntegerType.INT32, 0));
                } else {
                    instr = new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(),
                            symbol.getIRValue(), exps.get(0).generateIR(table));
                    return new IRInstrLoad(IRBuilder.getInstance().getLocalVarName(), instr, true);
                }
            case 2:
                switch (requireDim) {
                    case 0:
                        return new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(),
                                symbol.getIRValue(), new IRConstInt(IRIntegerType.INT32, 0));
                    case 1:
                        instr = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(),
                                IRInstrType.MUL, true,
                                new IRConstInt(IRIntegerType.INT32, symbol.getLength(2)),
                                exps.get(0).generateIR(table));
                        return new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(),
                                symbol.getIRValue(), instr);
                    case 2:
                        instr = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(),
                                IRInstrType.MUL, true,
                                new IRConstInt(IRIntegerType.INT32, symbol.getLength(2)),
                                exps.get(0).generateIR(table));
                        instr = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(),
                                IRInstrType.ADD, true,
                                instr, exps.get(1).generateIR(table));
                        instr = new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(),
                                symbol.getIRValue(), instr);
                        return new IRInstrLoad(IRBuilder.getInstance().getLocalVarName(), instr, true);
                    default:
                        System.out.println("wrong1 in LVal.generateIRForValue");
                        return null;
                }
            default:
                System.out.println("wrong2 in LVal.generateIRForValue");
                return null;
        }
    }

    public IRValue generateIRForAssign(SymbolTable table) { //仅用于 LVal 被赋值
        SymbolVar symbol = (SymbolVar) table.getSymbol(ident.getName()); //只有变量可以被赋值
        switch (symbol.getDim()) {
            case 0:
                return symbol.getIRValue();
            case 1:
                IRValue v1 = exps.get(0).generateIR(table);
                return new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(), symbol.getIRValue(), v1);
            case 2:
                IRValue v21 = exps.get(0).generateIR(table);
                IRValue v22 = exps.get(1).generateIR(table);
                IRInstr i = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.MUL, true, v21,
                        new IRConstInt(IRIntegerType.INT32, symbol.getLength(2)));
                i = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.ADD, true, v22,
                        i);
                return new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(), symbol.getIRValue(), i);
            default:
                System.out.println("wrong in LVal.generateIRForAssign");
                return null;
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        for(Exp e : exps){
            sb.append("LBRACK [\n");
            sb.append(e);
            sb.append("RBRACK ]\n");
        }
        sb.append("<LVal>\n");
        return sb.toString();
    }
}

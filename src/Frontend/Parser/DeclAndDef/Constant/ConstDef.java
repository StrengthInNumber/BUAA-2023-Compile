package Frontend.Parser.DeclAndDef.Constant;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.Constant.IRConstArray;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.GlobalVar.IRGlobalVar;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrGEP;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrAlloca;
import Middle.LLVMIR.Instruction.MemoryInstr.IRInstrStore;
import Middle.LLVMIR.Type.IRArrayType;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.Symbol.SymbolConst;
import Middle.Symbol.SymbolTable;
import Middle.Symbol.SymbolVar;
import Middle.Symbol.ValueType;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class ConstDef extends ASTNode {
    //常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    //包含普通变量、一维数组、二维数组共三种情况
    private Ident ident;
    private ArrayList<ConstExp> constExps;
    private ConstInitVal constInitVal;
    private int lineNum;

    public ConstDef(TokensReadControl tokens) {
        super(tokens);
        constExps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        ident = new Ident(tokens.getNowToken());
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        while (tokens.getNowTokenType() == TokenType.LBRACK) {
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
        if (tokens.getNowTokenType() != TokenType.ASSIGN) {
            printError();
        }
        tokens.nextToken();
        constInitVal = new ConstInitVal(tokens);
        constInitVal.parse();
    }

    public void checkError(SymbolTable table, ValueType type) {
        table.addSymbol(new SymbolConst(lineNum, ident.getName(), constExps.size(), type), false);
        for (ConstExp ce : constExps) {
            ce.checkError(table);
        }
        constInitVal.checkError(table);
    }

    public void generateIRGlobal(SymbolTable table, ValueType type) {
        ArrayList<Integer> lengths = new ArrayList<>();
        for (ConstExp ce : constExps) {
            lengths.add(ce.getConstValue(table));
        }
        SymbolConst sc = new SymbolConst(lineNum, ident.getName(), constExps.size(), type, lengths);
        table.addSymbol(sc, false);
        String name = IRBuilder.getInstance().getGlobalVarName();
        boolean isConst = true;
        IRIntegerType irType = IRIntegerType.INT32;
        IRGlobalVar gv;
        switch (constExps.size()) {
            case 0: //普通常量
                int init0 = constInitVal.getConstValue_0(table);
                sc.setInitVal_0(init0);
                IRConstInt constInt = new IRConstInt(irType, init0);
                gv = new IRGlobalVar(name, irType, isConst, constInt, true, false);
                sc.setIRValue(gv);
                break;
            case 1: //一维数组
                ArrayList<Integer> init1 = constInitVal.getConstValue_1(table);
                sc.setInitVal_1(init1);
                ArrayList<IRConstInt> init1IR = initIntToIRConst(init1);
                IRConstArray constArray1 = new IRConstArray(irType, init1IR);
                gv = new IRGlobalVar(name, new IRArrayType(init1IR.size(), irType), isConst,
                        constArray1, true, false);
                sc.setIRValue(gv);
                break;
            case 2: //二维数组
                ArrayList<ArrayList<Integer>> init2 = constInitVal.getConstValue_2(table);
                sc.setInitVal_2(init2);
                ArrayList<IRConstInt> init2IR = initIntToIRConst(init2ToInit1(init2));
                IRConstArray constArray2 = new IRConstArray(irType, init2IR);
                gv = new IRGlobalVar(name, new IRArrayType(init2IR.size(), irType), isConst,
                        constArray2, true, false);
                sc.setIRValue(gv);
                break;
            default:
                System.out.println("wrong in ConstDef.generateIR: dimensions out of range");
                break;
        }
    }

    public void generateIRLocal(SymbolTable table, ValueType type) {
        ArrayList<Integer> lengths = new ArrayList<>();
        for (ConstExp ce : constExps) {
            lengths.add(ce.getConstValue(table));
        }
        SymbolConst sc = new SymbolConst(lineNum, ident.getName(), constExps.size(), type, lengths);
        table.addSymbol(sc, false);
        IRIntegerType irType = IRIntegerType.INT32;
        switch (constExps.size()) {
            case 0: //普通常量
                int init0 = constInitVal.getConstValue_0(table);
                sc.setInitVal_0(init0);
                IRInstr i0 = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(),
                        IRIntegerType.INT32, true);
                sc.setIRValue(i0);
                new IRInstrStore(new IRConstInt(IRIntegerType.INT32, init0), i0, true);
                break;
            case 1: //一维数组
                IRArrayType arrayType1 = new IRArrayType(sc.getLength(1), irType);
                IRInstr i1 = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(), arrayType1, true);
                sc.setIRValue(i1);
                ArrayList<Integer> values1 = constInitVal.getConstValue_1(table);
                sc.setInitVal_1(values1);
                int offset1 = 0;
                for (int i : values1) {
                    IRInstr iv = new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(), i1,
                            new IRConstInt(IRIntegerType.INT32, offset1));
                    new IRInstrStore(new IRConstInt(IRIntegerType.INT32, i), iv, true);
                    offset1++;
                }
                break;
            case 2: //二维数组
                IRArrayType arrayType2 = new IRArrayType(
                        sc.getLength(1) * sc.getLength(2), irType);
                IRInstr i2 = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(), arrayType2, true);
                sc.setIRValue(i2);
                ArrayList<ArrayList<Integer>> values2 = constInitVal.getConstValue_2(table);
                sc.setInitVal_2(values2);
                int offset2 = 0;
                for (ArrayList<Integer> j : values2) {
                    for(int k : j) {
                        IRInstr iv = new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(), i2,
                                new IRConstInt(IRIntegerType.INT32, offset2));
                        new IRInstrStore(new IRConstInt(IRIntegerType.INT32, k), iv, true);
                        offset2++;
                    }
                }
                break;
            default:
                System.out.println("wrong in ConstDef.generateIR: dimensions out of range");
                break;
        }
    }

    private ArrayList<Integer> init2ToInit1(ArrayList<ArrayList<Integer>> init2) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (ArrayList<Integer> integers : init2) {
            ans.addAll(integers);
        }
        return ans;
    }

    private ArrayList<IRConstInt> initIntToIRConst(ArrayList<Integer> ints) {
        ArrayList<IRConstInt> constInts = new ArrayList<>();
        for (int i : ints) {
            constInts.add(new IRConstInt(IRIntegerType.INT32, i));
        }
        return constInts;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        for (ConstExp ce : constExps) {
            sb.append("LBRACK [\n");
            sb.append(ce);
            sb.append("RBRACK ]\n");
        }
        sb.append("ASSIGN =\n");
        sb.append(constInitVal);
        sb.append("<ConstDef>\n");
        return sb.toString();
    }
}

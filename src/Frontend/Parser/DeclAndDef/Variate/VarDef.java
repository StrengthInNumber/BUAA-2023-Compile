package Frontend.Parser.DeclAndDef.Variate;

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
import Middle.LLVMIR.Type.IRType;
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

public class VarDef extends ASTNode {
    //变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
    // 包含普通变量、一维数组、二维数组定义
    private Ident ident;
    private ArrayList<ConstExp> constExps;
    private InitVal initVal;
    private int flag; //0-no initVal; 1-have initVal
    private int lineNum;

    public VarDef(TokensReadControl tokens) {
        super(tokens);
        constExps = new ArrayList<>();
        flag = 0;
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
        if (tokens.getNowTokenType() == TokenType.ASSIGN) {
            tokens.nextToken();
            flag = 1;
            initVal = new InitVal(tokens);
            initVal.parse();
        }
    }

    public void checkError(SymbolTable table, ValueType type) {
        table.addSymbol(new SymbolVar(lineNum, ident.getName(), constExps.size(), type), false);
        for (ConstExp cd : constExps) {
            cd.checkError(table);
        }
        if (flag == 1) {
            initVal.checkError(table);
        }
    }

    public void generateIRLocal(SymbolTable table, ValueType type) {
        ArrayList<Integer> lengths = new ArrayList<>();
        for(ConstExp ce : constExps) {
            lengths.add(ce.getConstValue(table));
        }
        SymbolVar sc = new SymbolVar(lineNum, ident.getName(), constExps.size(), type, lengths);
        table.addSymbol(sc, false);
        IRIntegerType irType = IRIntegerType.INT32;
        switch (constExps.size()) {
            case 0: //普通常量
                IRInstr i0 = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(),
                        irType, true);
                sc.setIRValue(i0);
                if(flag != 0) { //有初值
                    new IRInstrStore(initVal.getInitValue_0(table), i0, true);
                }
                break;
            case 1: //一维数组
                IRArrayType arrayType1 = new IRArrayType(sc.getLength(1), irType);
                IRInstr i1 = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(), arrayType1, true);
                sc.setIRValue(i1);
                if(flag != 0) {
                    ArrayList<IRValue> values = initVal.getInitValue_1(table);
                    int offset = 0;
                    for(IRValue v : values) {
                        IRInstr iv = new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(), i1,
                                new IRConstInt(IRIntegerType.INT32, offset));
                        new IRInstrStore(v, iv, true);
                        offset++;
                    }
                }
                break;
            case 2: //二维数组
                IRArrayType arrayType2 = new IRArrayType(
                        sc.getLength(1)*sc.getLength(2), irType);
                IRInstr i2 = new IRInstrAlloca(IRBuilder.getInstance().getLocalVarName(), arrayType2, true);
                sc.setIRValue(i2);
                if(flag != 0) {
                    ArrayList<IRValue> values = initVal.getInitValue_2(table);
                    int offset = 0;
                    for(IRValue v : values) {
                        IRInstr iv = new IRInstrGEP(IRBuilder.getInstance().getLocalVarName(), i2,
                                new IRConstInt(IRIntegerType.INT32, offset));
                        new IRInstrStore(v, iv, true);
                        offset++;
                    }
                }
                break;
            default:
                System.out.println("wrong in ConstDef.generateIR: dimensions out of range");
                break;
        }
    }

    public void generateIRGlobal(SymbolTable table, ValueType type) {
        ArrayList<Integer> lengths = new ArrayList<>();
        for(ConstExp ce : constExps) {
            lengths.add(ce.getConstValue(table));
        }
        SymbolVar sc = new SymbolVar(lineNum, ident.getName(), constExps.size(), type, lengths);
        table.addSymbol(sc, false);
        String name = IRBuilder.getInstance().getGlobalVarName();
        boolean isConst = false;
        IRIntegerType irType = IRIntegerType.INT32;
        IRGlobalVar gv = null;
        boolean isZeroInitialize = false;
        switch (constExps.size()) {
            case 0: //普通常量
                int init0;
                if (flag == 0) {
                    init0 = 0;
                } else {
                    init0 = initVal.getConstValue_0(table);
                }
                sc.setInitVal_0(init0);
                IRConstInt constInt = new IRConstInt(irType, init0);
                gv = new IRGlobalVar(name, irType, isConst, constInt, true, false);
                sc.setIRValue(gv);
                break;
            case 1: //一维数组
                ArrayList<Integer> init1;
                if (flag == 0) {
                    init1 = generate0Array_1(constExps.get(0).getConstValue(table));
                    isZeroInitialize = true;
                } else {
                    init1 = initVal.getConstValue_1(table);
                }
                sc.setInitVal_1(init1);
                ArrayList<IRConstInt> init1IR = initIntToIRConst(init1);
                IRConstArray constArray1 = new IRConstArray(irType, init1IR);
                gv = new IRGlobalVar(name, new IRArrayType(init1IR.size(), irType),
                        isConst, constArray1, true, isZeroInitialize);
                sc.setIRValue(gv);
                break;
            case 2: //二维数组
                ArrayList<ArrayList<Integer>> init2;
                if (flag == 0) {
                    init2 = generate0Array_2(constExps.get(0).getConstValue(table),
                            constExps.get(1).getConstValue(table));
                    isZeroInitialize = true;
                } else {
                    init2 = initVal.getConstValue_2(table);
                }
                sc.setInitVal_2(init2);
                ArrayList<IRConstInt> init2IR = initIntToIRConst(init2ToInit1(init2));
                IRConstArray constArray2 = new IRConstArray(irType, init2IR);
                gv = new IRGlobalVar(name, new IRArrayType(init2IR.size(), irType),
                        isConst, constArray2, true, isZeroInitialize);
                sc.setIRValue(gv);
                break;
            default:
                System.out.println("wrong in ConstDef.generateIR: dimensions out of range");
                break;
        }
    }

    private ArrayList<Integer> generate0Array_1(int len) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            ans.add(0);
        }
        return ans;
    }

    private ArrayList<ArrayList<Integer>> generate0Array_2(int i, int j) {
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        for (int p = 0; p < i; p++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int q = 0; q < j; q++) {
                temp.add(0);
            }
            ans.add(temp);
        }
        return ans;
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
        for (ConstExp cd : constExps) {
            sb.append("LBRACK [\n");
            sb.append(cd);
            sb.append("RBRACK ]\n");
        }
        if (flag == 1) {
            sb.append("ASSIGN =\n");
            sb.append(initVal);
        }
        sb.append("<VarDef>\n");
        return sb.toString();
    }
}

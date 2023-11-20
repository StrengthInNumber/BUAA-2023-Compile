package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrIcmp;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.LLVMIR.Instruction.IRInstrZext;
import Middle.LLVMIR.Type.IRIntegerType;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class RelExp extends ASTNode {
    //RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    private ArrayList<AddExp> addExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;

    public RelExp(TokensReadControl tokens) {
        super(tokens);
        addExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.GRE);
        opts.add(TokenType.LSS);
        opts.add(TokenType.GEQ);
        opts.add(TokenType.LEQ);
    }

    public void parse() throws CompilerError {
        AddExp addExp = new AddExp(tokens);
        addExp.parse();
        addExps.add(addExp);
        optList.add(null);
        while (opts.contains(tokens.getNowTokenType())) {
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            addExp = new AddExp(tokens);
            addExp.parse();
            addExps.add(addExp);
        }
    }

    public void checkError(SymbolTable table) {
        for (AddExp e : addExps) {
            e.checkError(table);
        }
    }

    public IRValue generateIR(SymbolTable table) {
        IRValue v1 = addExps.get(0).generateIR(table);
        IRValue v2;
        for (int i = 1; i < addExps.size(); i++) {
            v2 = addExps.get(i).generateIR(table);
            if (v1.getType() != IRIntegerType.INT32) {
                v1 = new IRInstrZext(IRBuilder.getInstance().getLocalVarName(), true,
                        v1, IRIntegerType.INT32);
            }
            if (v2.getType() != IRIntegerType.INT32) {
                v2 = new IRInstrZext(IRBuilder.getInstance().getLocalVarName(), true,
                        v2, IRIntegerType.INT32);
            }
            IRInstrType instrType;
            switch (optList.get(i).getContent()) {
                case ">":
                    instrType = IRInstrType.SGT;
                    break;
                case "<":
                    instrType = IRInstrType.SLT;
                    break;
                case ">=":
                    instrType = IRInstrType.SGE;
                    break;
                case "<=":
                    instrType = IRInstrType.SLE;
                    break;
                default:
                    instrType = null;
                    System.out.println("wrong in RelExp.generateIR");
                    break;
            }
            v1 = new IRInstrIcmp(IRBuilder.getInstance().getLocalVarName(), instrType, true, v1, v2);
        }
        return v1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(addExps.get(0));
        sb.append("<RelExp>\n");
        for (int i = 1; i < addExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(addExps.get(i));
            sb.append("<RelExp>\n");
        }
        return sb.toString();
    }
}

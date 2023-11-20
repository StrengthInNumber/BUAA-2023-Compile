package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.Constant.IRConstInt;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
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

public class EqExp extends ASTNode {
    //EqExp → RelExp | EqExp ('==' | '!=') RelExp
    private ArrayList<RelExp> relExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;

    public EqExp(TokensReadControl tokens) {
        super(tokens);
        relExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.EQL);
        opts.add(TokenType.NEQ);
    }

    public void parse() throws CompilerError {
        RelExp relExp = new RelExp(tokens);
        relExp.parse();
        relExps.add(relExp);
        optList.add(null);
        while (opts.contains(tokens.getNowTokenType())) {
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            relExp = new RelExp(tokens);
            relExp.parse();
            relExps.add(relExp);
        }
    }

    public void checkError(SymbolTable table) {
        for (RelExp e : relExps) {
            e.checkError(table);
        }
    }

    public IRValue generateIR(SymbolTable table) {
        IRValue v1 = relExps.get(0).generateIR(table);
        IRValue v2;
        for (int i = 1; i < relExps.size(); i++) {
            v2 = relExps.get(i).generateIR(table);
            if(v1.getType() != IRIntegerType.INT32) {
                v1 = new IRInstrZext(IRBuilder.getInstance().getLocalVarName(), true,
                        v1, IRIntegerType.INT32);
            }
            if(v2.getType() != IRIntegerType.INT32) {
                v2 = new IRInstrZext(IRBuilder.getInstance().getLocalVarName(), true,
                        v2, IRIntegerType.INT32);
            }
            if(optList.get(i).getContent().equals("==")) {
                v1 = new IRInstrIcmp(IRBuilder.getInstance().getLocalVarName(), IRInstrType.EQ,
                        true, v1, v2);
            } else {
                v1 = new IRInstrIcmp(IRBuilder.getInstance().getLocalVarName(), IRInstrType.NE,
                        true, v1, v2);
            }
        }
        if(relExps.size() == 1) {
            if(v1.getType() != IRIntegerType.INT32) {
                v1 = new IRInstrZext(IRBuilder.getInstance().getLocalVarName(), true,
                        v1, IRIntegerType.INT32);
            }
            v1 = new IRInstrIcmp(IRBuilder.getInstance().getLocalVarName(), IRInstrType.NE,
                    true, v1, new IRConstInt(IRIntegerType.INT32, 0));
        }
        return v1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(relExps.get(0));
        sb.append("<EqExp>\n");
        for (int i = 1; i < relExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(relExps.get(i));
            sb.append("<EqExp>\n");
        }
        return sb.toString();
    }
}

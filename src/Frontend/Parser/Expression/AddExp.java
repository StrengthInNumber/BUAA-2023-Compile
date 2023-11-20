package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstr;
import Middle.LLVMIR.Instruction.IRInstrAlu;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class AddExp extends ASTNode {
    //AddExp  → MulExp | AddExp ('+' | '−') MulExp
    private ArrayList<MulExp> mulExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;

    public AddExp(TokensReadControl tokens) {
        super(tokens);
        mulExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.PLUS);
        opts.add(TokenType.MINU);
    }

    public void parse() throws CompilerError {
        MulExp mulExp = new MulExp(tokens);
        mulExp.parse();
        mulExps.add(mulExp);
        optList.add(null);
        while (opts.contains(tokens.getNowTokenType())) {
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            mulExp = new MulExp(tokens);
            mulExp.parse();
            mulExps.add(mulExp);
        }
    }

    public void checkError(SymbolTable table){
        for(MulExp m : mulExps){
            m.checkError(table);
        }
    }

    public int getConstValue(SymbolTable table){
        int ans = mulExps.get(0).getConstValue(table);
        for(int i = 1; i < mulExps.size(); i++){
            if(optList.get(i).getContent().equals("+")) {
                ans += mulExps.get(i).getConstValue(table);
            } else {
                ans -= mulExps.get(i).getConstValue(table);
            }
        }
        return ans;
    }

    public IRValue generateIR(SymbolTable table) {
        IRValue v1 = mulExps.get(0).generateIR(table);
        IRValue v2;
        for(int i = 1; i < mulExps.size() ; i++) {
            v2 = mulExps.get(i).generateIR(table);
            if(optList.get(i).getContent().equals("+")) {
                v1 = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.ADD,
                        true, v1, v2);
            } else {
                v1 = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.SUB,
                        true, v1, v2);
            }
        }
        return v1;
    }
    public int getDim(SymbolTable table){
        return mulExps.get(0).getDim(table);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mulExps.get(0));
        sb.append("<AddExp>\n");
        for (int i = 1; i < mulExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(mulExps.get(i));
            sb.append("<AddExp>\n");
        }
        return sb.toString();
    }
}

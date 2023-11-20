package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRValue;
import Middle.LLVMIR.Instruction.IRInstrAlu;
import Middle.LLVMIR.Instruction.IRInstrType;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;
import java.util.HashSet;

public class MulExp extends ASTNode {
    //MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    private ArrayList<UnaryExp> unaryExps;
    private ArrayList<Token> optList;
    private HashSet<TokenType> opts;
    public MulExp(TokensReadControl tokens){
        super(tokens);
        unaryExps = new ArrayList<>();
        optList = new ArrayList<>();
        opts = new HashSet<>();
        opts.add(TokenType.MULT);
        opts.add(TokenType.DIV);
        opts.add(TokenType.MOD);
    }

    public void parse() throws CompilerError {
        UnaryExp unaryExp = new UnaryExp(tokens);
        unaryExp.parse();
        unaryExps.add(unaryExp);
        optList.add(null);
        while(opts.contains(tokens.getNowTokenType())){
            optList.add(tokens.getNowToken());
            tokens.nextToken();
            unaryExp = new UnaryExp(tokens);
            unaryExp.parse();
            unaryExps.add(unaryExp);
        }
    }
    public void checkError(SymbolTable table){
        for(UnaryExp m : unaryExps){
            m.checkError(table);
        }
    }

    public int getConstValue(SymbolTable table) {
        int ans = unaryExps.get(0).getConstValue(table);
        for(int i = 1; i < unaryExps.size(); i++){
            if(optList.get(i).getContent().equals("*")) {
                ans *= unaryExps.get(i).getConstValue(table);
            } else if(optList.get(i).getContent().equals("/")) {
                ans /= unaryExps.get(i).getConstValue(table);
            } else { //%
                ans %= unaryExps.get(i).getConstValue(table);
            }
        }
        return ans;
    }

    public IRValue generateIR(SymbolTable table) {
        IRValue v1 = unaryExps.get(0).generateIR(table);
        IRValue v2;
        for(int i = 1; i < unaryExps.size() ; i++) {
            v2 = unaryExps.get(i).generateIR(table);
            if(optList.get(i).getContent().equals("*")) {
                v1 = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.MUL,
                        true, v1, v2);
            } else if(optList.get(i).getContent().equals("/")) {
                v1 = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.SDIV,
                        true, v1, v2);
            } else { //%
                v1 = new IRInstrAlu(IRBuilder.getInstance().getLocalVarName(), IRInstrType.SREM,
                        true, v1, v2);
            }
        }
        return v1;
    }
    public int getDim(SymbolTable table){
        return unaryExps.get(0).getDim(table);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(unaryExps.get(0));
        sb.append("<MulExp>\n");
        for (int i = 1; i < unaryExps.size(); i++) {
            sb.append(optList.get(i));
            sb.append(unaryExps.get(i));
            sb.append("<MulExp>\n");
        }
        return sb.toString();
    }
}

package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.LLVMIR.BasicBlock.IRBasicBlock;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.Instruction.IRInstrBranch;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class LAndExp extends ASTNode {
    private final ArrayList<EqExp> eqExps;

    public LAndExp(TokensReadControl tokens) {
        super(tokens);
        eqExps = new ArrayList<>();
    }

    public void generateIR(SymbolTable table, IRBasicBlock trueBB, IRBasicBlock falseBB) {
        for (int i = 0; i < eqExps.size() - 1; i++) {
            IRBasicBlock nextBB = new IRBasicBlock(IRBuilder.getInstance().getBasicBlockName(), true);
            new IRInstrBranch(eqExps.get(i).generateIR(table), nextBB, falseBB, true);
            IRBuilder.getInstance().setCurBasicBlock(nextBB);
        }
        new IRInstrBranch(eqExps.get(eqExps.size() - 1).generateIR(table), trueBB, falseBB, true);
    }
    public void parse() throws CompilerError {
        EqExp eqExp = new EqExp(tokens);
        eqExp.parse();
        eqExps.add(eqExp);
        while (tokens.getNowTokenType() == TokenType.AND) {
            tokens.nextToken();
            eqExp = new EqExp(tokens);
            eqExp.parse();
            eqExps.add(eqExp);
        }
    }
    public void checkError(SymbolTable table){
        for(EqExp e : eqExps){
            e.checkError(table);
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(eqExps.get(0));
        sb.append("<LAndExp>\n");
        for (int i = 1; i < eqExps.size(); i++) {
            sb.append("AND &&\n");
            sb.append(eqExps.get(i));
            sb.append("<LAndExp>\n");
        }
        return sb.toString();
    }
}

package Frontend.Parser.Expression;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.IRValue;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncRParams extends ASTNode {
    //函数实参表 FuncRParams → Exp { ',' Exp }
    // 1.花括号内重复0次 2.花括号内重复多次 3.Exp需要覆盖数组传参和部分数组传参
    private Exp exp;
    private ArrayList<Exp> exps;
    private int lineNum;

    public FuncRParams(TokensReadControl tokens) {
        super(tokens);
        exps = new ArrayList<>();
        exp = new Exp(tokens);
    }

    public void parse() throws CompilerError {
        exp.parse();
        lineNum = tokens.getNowTokenLineNum();
        while (tokens.getNowTokenType() == TokenType.COMMA) {
            tokens.nextToken();
            Exp e = new Exp(tokens);
            e.parse();
            exps.add(e);
        }
    }

    public void checkError(SymbolTable table, String name){
        if(table.getFuncParamSize(name) != exps.size() + 1){
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.PARAM_NUM_MISMATCH));
        } else {
            ArrayList<Integer> dims = table.getFuncDims(name);
            for(int i = 0; i < dims.size(); i++){
                if(i == 0){
                    if(dims.get(0) != exp.getDim(table)){
                        ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.PARAM_TYPE_MISMATCH));
                        break;
                    }
                } else {
                    if(dims.get(i) != exps.get(i - 1).getDim(table)){
                        ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.PARAM_TYPE_MISMATCH));
                        break;
                    }
                }
            }
        }
        exp.checkError(table);
        for(Exp e : exps){
            e.checkError(table);
        }
    }

    public ArrayList<IRValue> generateIR(SymbolTable table) {
        ArrayList<IRValue> ans = new ArrayList<>();
        ans.add(exp.generateIR(table));
        for(Exp e : exps) {
            ans.add(e.generateIR(table));
        }
        return ans;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(exp);
        for(Exp e : exps){
            sb.append("COMMA ,\n");
            sb.append(e);
        }
        sb.append("<FuncRParams>\n");
        return sb.toString();
    }
}

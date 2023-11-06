package Frontend.Parser.Expression;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolConst;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

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

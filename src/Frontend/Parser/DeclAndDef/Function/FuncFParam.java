package Frontend.Parser.DeclAndDef.Function;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolConst;
import Check.Symbol.SymbolTable;
import Check.Symbol.SymbolVar;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.ConstExp;
import Frontend.Parser.Terminator.BType;
import Frontend.Parser.Terminator.Ident;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class FuncFParam extends ASTNode {
    //函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    //ConstExp即为 AddExp
    // 1.普通变量2.一维 数组变量 3.二维数组变量
    private BType bType;
    private Ident ident;
    private ArrayList<ConstExp> constExps;
    private int flag; //0-普通变量 1-一维数组 2-二维数组
    private int lineNum;

    public FuncFParam(TokensReadControl tokens) {
        super(tokens);
        constExps = new ArrayList<>();
        flag = 0;
    }

    public void parse() throws CompilerError {
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        ident = new Ident(tokens.getNowToken());
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if (tokens.getNowTokenType() == TokenType.LBRACK) {
            flag = 1;
            tokens.nextToken();
            if (tokens.getNowTokenType() != TokenType.RBRACK) {
                //throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
                ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RBRACK));
            } else {
                tokens.nextToken();
            }
            while (tokens.getNowTokenType() == TokenType.LBRACK) {
                flag = 2;
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
        }
    }

    public void checkError(SymbolTable table) {
        int dim;
        if(flag == 0){
            dim = 0;
        } else {
            dim = constExps.size() + 1;
        }
        table.addSymbol(new SymbolVar(lineNum, ident.getName(), dim, bType.getValueType()), true);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType);
        sb.append(ident);
        if (flag != 0) {
            sb.append("LBRACK [\n");
            sb.append("RBRACK ]\n");
            for (ConstExp ce : constExps) {
                sb.append("LBRACK [\n");
                sb.append(ce);
                sb.append("RBRACK ]\n");
            }
        }
        sb.append("<FuncFParam>\n");
        return sb.toString();
    }
}

package Frontend.Parser.DeclAndDef.Variate;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolConst;
import Check.Symbol.SymbolTable;
import Check.Symbol.SymbolVar;
import Check.Symbol.ValueType;
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

    public VarDef(TokensReadControl tokens){
        super(tokens);
        constExps = new ArrayList<>();
        flag = 0;
    }

    public void parse() throws CompilerError {
        ident = new Ident(tokens.getNowToken());
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        while(tokens.getNowTokenType() == TokenType.LBRACK){
            tokens.nextToken();
            ConstExp constExp = new ConstExp(tokens);
            constExp.parse();
            constExps.add(constExp);
            if(tokens.getNowTokenType() != TokenType.RBRACK){
                //throw new CompilerError(ErrorType.MISS_RBRACK, tokens.getNowTokenLineNum());
                ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RBRACK));
            }else {
                tokens.nextToken();
            }
        }
        if(tokens.getNowTokenType() == TokenType.ASSIGN){
            tokens.nextToken();
            flag = 1;
            initVal = new InitVal(tokens);
            initVal.parse();
        }
    }

    public void checkError(SymbolTable table, ValueType type){
        table.addSymbol(new SymbolVar(lineNum, ident.getName(), constExps.size(), type), false);
        for(ConstExp cd : constExps){
            cd.checkError(table);
        }
        if(flag == 1){
            initVal.checkError(table);
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ident);
        for(ConstExp cd : constExps){
            sb.append("LBRACK [\n");
            sb.append(cd);
            sb.append("RBRACK ]\n");
        }
        if(flag == 1){
            sb.append("ASSIGN =\n");
            sb.append(initVal);
        }
        sb.append("<VarDef>\n");
        return sb.toString();
    }
}

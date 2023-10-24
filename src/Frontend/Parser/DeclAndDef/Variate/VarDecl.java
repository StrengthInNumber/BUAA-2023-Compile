package Frontend.Parser.DeclAndDef.Variate;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Check.Symbol.SymbolTable;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Terminator.BType;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class VarDecl extends ASTNode {
    //变量声明 VarDecl → BType VarDef { ',' VarDef } ';'
    // 1.花括号内重复0次 2.花括号内重复 多次
    private BType bType;
    private VarDef varDef;
    private ArrayList<VarDef> varDefs;
    public VarDecl(TokensReadControl tokens){
        super(tokens);
        varDefs = new ArrayList<>();
        varDef = new VarDef(tokens);
    }

    public void parse() throws CompilerError {
        bType = new BType(tokens.getNowToken());
        tokens.nextToken();
        varDef.parse();
        while(tokens.getNowTokenType() == TokenType.COMMA){
            tokens.nextToken();
            VarDef vd = new VarDef(tokens);
            vd.parse();
            varDefs.add(vd);
        }
        if(tokens.getNowTokenType() != TokenType.SEMICN){
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_SEMICN));
        } else {
            tokens.nextToken();
        }
    }

    public void checkError(SymbolTable table){
        varDef.checkError(table, bType.getValueType());
        for(VarDef vd : varDefs){
            vd.checkError(table, bType.getValueType());
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bType);
        sb.append(varDef);
        for(VarDef vd : varDefs){
            sb.append("COMMA ,\n");
            sb.append(vd);
        }
        sb.append("SEMICN ;\n");
        sb.append("<VarDecl>\n");
        return sb.toString();
    }
}

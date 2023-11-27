package Frontend.Parser.BlockAndStmt.Statements;

import Middle.CompilerError;
import Middle.Error.Error;
import Middle.Error.ErrorTable;
import Middle.Error.ErrorType;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRString;
import Middle.LLVMIR.Instruction.IOInstr.IRInstrPutCh;
import Middle.LLVMIR.Instruction.IOInstr.IRInstrPutInt;
import Middle.Symbol.SymbolTable;
import Frontend.Lexer.Token.Token;
import Frontend.Lexer.Token.TokenType;
import Frontend.Parser.ASTNode;
import Frontend.Parser.Expression.Exp;
import Frontend.TokensReadControl;

import java.util.ArrayList;

public class PrintfOpt extends ASTNode implements StmtOpt{
    //PrintfStmt → 'printf''('FormatString{','Exp}')'';'
    // 1.有Exp 2.无Exp
    public Token formatString;
    public ArrayList<Exp> exps;
    public int lineNum;
    public PrintfOpt(TokensReadControl tokens){
        super(tokens);
        exps = new ArrayList<>();
    }

    public void parse() throws CompilerError {
        if(tokens.getNowTokenType() != TokenType.PRINTFTK){
            printError();
        }
        lineNum = tokens.getNowTokenLineNum();
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.LPARENT){
            printError();
        }
        tokens.nextToken();
        if(tokens.getNowTokenType() != TokenType.STRCON){
            printError();
        }
        formatString = tokens.getNowToken();
        tokens.nextToken();
        while(tokens.getNowTokenType() == TokenType.COMMA){
            tokens.nextToken();
            Exp exp = new Exp(tokens);
            exp.parse();
            exps.add(exp);
        }
        if(tokens.getNowTokenType() != TokenType.RPARENT){
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_RPARENT));
        }
        tokens.nextToken();
        if (tokens.getNowTokenType() != TokenType.SEMICN) {
            //throw new CompilerError(ErrorType.MISS_SEMICN, tokens.getNowTokenLineNum());
            ErrorTable.getInstance().addError(new Error(tokens.getLastTokenLineNum(), ErrorType.MISS_SEMICN));
        } else {
            tokens.nextToken();
        }
    }

    @Override
    public void checkError(SymbolTable table) {
        String s = formatString.getContent();
        int count = 0;
        for(int i = 0; i < s.length() - 1; i++){
            if(s.charAt(i) == '%' && s.charAt(i + 1) == 'd'){
                count++;
            }
        }
        if(count != exps.size()){
            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.FORM_STRING_MISMATCH));
        }
    }

    public void generateIR(SymbolTable table) {
        String fs = formatString.getContent();
        int expNum = 0;
        IRString irString = new IRString(IRBuilder.getInstance().getIRStringName());
        for(int i = 0; i < fs.length(); i++) {
            if(fs.charAt(i) == '%') {
                new IRInstrPutInt(exps.get(expNum).generateIR(table), true);
                i++;
                expNum++;
                irString = new IRString(IRBuilder.getInstance().getIRStringName());
            } else if (fs.charAt(i) != '\"') {
                if(fs.charAt(i) == '\\' && fs.charAt(i + 1) == 'n') {
                    irString.addChar(fs.charAt(i));
                    irString.addChar(fs.charAt(i + 1));
                    new IRInstrPutCh('\n', true, irString);
                    i++;
                } else {
                    irString.addChar(fs.charAt(i));
                    new IRInstrPutCh(fs.charAt(i), true, irString);
                }
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PRINTFTK printf\n");
        sb.append("LPARENT (\n");
        sb.append(formatString);
        for(Exp e : exps){
            sb.append("COMMA ,\n");
            sb.append(e);
        }
        sb.append("RPARENT )\n");
        sb.append("SEMICN ;\n");
        return sb.toString();
    }
}

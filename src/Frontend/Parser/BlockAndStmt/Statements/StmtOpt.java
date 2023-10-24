package Frontend.Parser.BlockAndStmt.Statements;

import Check.CompilerError;
import Check.Symbol.SymbolTable;

public interface StmtOpt {
    public void parse() throws CompilerError;
    public void checkError(SymbolTable table);
}

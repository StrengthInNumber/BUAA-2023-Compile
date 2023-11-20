package Frontend.Parser.BlockAndStmt.Statements;

import Middle.CompilerError;
import Middle.Symbol.SymbolTable;

public interface StmtOpt {
    void parse() throws CompilerError;
    void checkError(SymbolTable table);
    void generateIR(SymbolTable table);
}

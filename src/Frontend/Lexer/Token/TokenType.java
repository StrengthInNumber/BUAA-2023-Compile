package Frontend.Lexer.Token;

public enum TokenType {
    //IntConst
    INTCON,

    //Identifier
    IDENFR,

    //FormatString
    STRCON,

    // Reserved Word
    MAINTK, CONSTTK, INTTK, BREAKTK,
    CONTINUETK, IFTK, ELSETK, RETURNTK,
    FORTK, VOIDTK, GETINTTK, PRINTFTK,

    // Simple Token
    NOT, AND, OR, PLUS,
    MINU, MULT, DIV, MOD,
    LSS, LEQ, GRE, GEQ,
    EQL, NEQ, ASSIGN, SEMICN,
    COMMA, LPARENT, RPARENT, LBRACK,
    RBRACK, LBRACE, RBRACE
}

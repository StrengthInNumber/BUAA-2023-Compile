package Frontend.Lexer;

import Check.CompilerError;
import Check.Error.Error;
import Check.Error.ErrorTable;
import Check.Error.ErrorType;
import Frontend.Lexer.Token.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private final String source;
    private final int sourceLen;
    private int curPos;
    private int lineNum;
    private final ArrayList<Token> tokens;
    private final HashMap<String, TokenType> reservedWords;
    private final HashMap<Character, TokenType> singleDelimiter;

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public Lexer(String source) {
        this.source = source;
        sourceLen = source.length();
        curPos = -1;
        lineNum = 1;
        tokens = new ArrayList<>();

        reservedWords = new HashMap<>();
        reservedWords.put("main", TokenType.MAINTK);
        reservedWords.put("const", TokenType.CONSTTK);
        reservedWords.put("int", TokenType.INTTK);
        reservedWords.put("break", TokenType.BREAKTK);
        reservedWords.put("continue", TokenType.CONTINUETK);
        reservedWords.put("if", TokenType.IFTK);
        reservedWords.put("else", TokenType.ELSETK);
        reservedWords.put("return", TokenType.RETURNTK);
        reservedWords.put("for", TokenType.FORTK);
        reservedWords.put("void", TokenType.VOIDTK);
        reservedWords.put("getint", TokenType.GETINTTK);
        reservedWords.put("printf", TokenType.PRINTFTK);

        singleDelimiter = new HashMap<>();
        singleDelimiter.put('+', TokenType.PLUS);
        singleDelimiter.put('-', TokenType.MINU);
        singleDelimiter.put('*', TokenType.MULT);
        singleDelimiter.put('/', TokenType.DIV);
        singleDelimiter.put('%', TokenType.MOD);
        singleDelimiter.put('>', TokenType.GRE);
        singleDelimiter.put('<', TokenType.LSS);
        singleDelimiter.put('=', TokenType.ASSIGN);
        singleDelimiter.put('!', TokenType.NOT);
        singleDelimiter.put(',', TokenType.COMMA);
        singleDelimiter.put(';', TokenType.SEMICN);
        singleDelimiter.put('(', TokenType.LPARENT);
        singleDelimiter.put(')', TokenType.RPARENT);
        singleDelimiter.put('[', TokenType.LBRACK);
        singleDelimiter.put(']', TokenType.RBRACK);
        singleDelimiter.put('{', TokenType.LBRACE);
        singleDelimiter.put('}', TokenType.RBRACE);
    }

    public void run() throws CompilerError {
        nextChar();
        while (notReachEnd()) {
            getToken();
        }
    }

    private char getNowChar() {
        return source.charAt(curPos);
    }

    private boolean notReachEnd() {
        return curPos < sourceLen;
    }

    private boolean isDigit(char c) { //数字
        return Character.isDigit(c);
    }

    private boolean isLetterAndUnderline(char c) { //字母和下划线
        return Character.isLetter(c) || c == '_';
    }

    private void nextChar() throws CompilerError {
        curPos++;
        if (notReachEnd() && getNowChar() == '@') { // 待完善！！！读到非法字符
            throw new CompilerError(ErrorType.ILLEGAL_SYMBOL, lineNum);
        }
    }

    public void getToken() throws CompilerError {
        StringBuilder nowToken = new StringBuilder();
        if (notReachEnd() && getNowChar() == '/') { //读到一个/
            nowToken.append(getNowChar());
            nextChar();
            if (notReachEnd() && getNowChar() == '/') { //读到第二个/ 单行注释
                curPos++;
                while (notReachEnd() && getNowChar() != '\n') {
                    curPos++;
                }
                lineNum++;
                curPos++;
            } else if (notReachEnd() && getNowChar() == '*') { //读到* 多行注释
                curPos++;
                while (notReachEnd()) {
                    while (notReachEnd() && getNowChar() != '*') {
                        if (getNowChar() == '\n') {
                            lineNum++;
                        }
                        curPos++;
                    }
                    while (notReachEnd() && getNowChar() == '*') {
                        curPos++;
                    }
                    if (notReachEnd() && getNowChar() == '/') {
                        nextChar();
                        break;
                    }
                }
            } else { // /后非/非*，即为除号
                tokens.add(new SimpleToken(TokenType.DIV, lineNum, "/"));
            }
        } else if (notReachEnd() && isLetterAndUnderline(getNowChar())) { // 读到字母下划线 标识符识别
            nowToken.append(getNowChar());
            nextChar();
            while (notReachEnd() && (isDigit(getNowChar()) || isLetterAndUnderline(getNowChar()))) {
                nowToken.append(getNowChar());
                nextChar();
            }
            if (reservedWords.containsKey(nowToken.toString())) {
                tokens.add(new ReservedWord(reservedWords.get(nowToken.toString()), lineNum, nowToken.toString()));
            } else {
                tokens.add(new Identifier(lineNum, nowToken.toString()));
            }
        } else if (notReachEnd() && isDigit(getNowChar())) { //读到数字
            nowToken.append(getNowChar());
            nextChar();
            if (nowToken.toString().equals("0")) { //根据文法 0只能单独出现 不能有00
                tokens.add(new IntConst(lineNum, nowToken.toString()));
            } else {
                while (notReachEnd() && isDigit(getNowChar())) {
                    nowToken.append(getNowChar());
                    nextChar();
                }
                tokens.add(new IntConst(lineNum, nowToken.toString()));
            }
            /*
                如果下一个字符是字母或者不在文法中出现的字符则需要报错
                同时，如果下一个字符是数字0（对应第一个数字读到0然后直接结束数字读取的情况）也需要报错
            */
        } else if (notReachEnd() && getNowChar() == '"') { //读到双引号，开始处理格式字符串
            nowToken.append(getNowChar());
            curPos++;
            while (notReachEnd() && getNowChar() != '"') {
                if (isFSLegal(getNowChar())) {
                    if (getNowChar() == 92) {
                        curPos++;
                        if (getNowChar() != 'n') {
                            ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.ILLEGAL_SYMBOL));
                            continue;
                        } else {
                            curPos--;
                        }
                    }
                    nowToken.append(getNowChar());
                    curPos++;
                } else if (getNowChar() == '%') {
                    curPos++;
                    if (getNowChar() == 'd') {
                        curPos++;
                        nowToken.append("%d");
                    } else {
                        ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.ILLEGAL_SYMBOL));
                    }
                } else {
                    ErrorTable.getInstance().addError(new Error(lineNum, ErrorType.ILLEGAL_SYMBOL));
                    curPos++;
                }

            }
            nowToken.append(getNowChar());
            nextChar();
            tokens.add(new FormatString(lineNum, nowToken.toString()));
        } else if (notReachEnd() && getNowChar() == '&') {
            nextChar();
            if (notReachEnd() && getNowChar() != '&') {
                throw new CompilerError(ErrorType.ILLEGAL_SYMBOL, lineNum);
            } else {
                tokens.add(new SimpleToken(TokenType.AND, lineNum, "&&"));
                nextChar();
            }
        } else if (notReachEnd() && getNowChar() == '|') {
            nextChar();
            if (notReachEnd() && getNowChar() != '|') {
                throw new CompilerError(ErrorType.ILLEGAL_SYMBOL, lineNum);
            } else {
                tokens.add(new SimpleToken(TokenType.OR, lineNum, "||"));
            }
            nextChar();
        } else if (notReachEnd() && getNowChar() == '<') {
            curPos++;
            if (notReachEnd() && getNowChar() == '=') {
                nextChar();
                tokens.add(new SimpleToken(TokenType.LEQ, lineNum, "<="));
            } else {
                curPos--;
                nextChar();
                tokens.add(new SimpleToken(TokenType.LSS, lineNum, "<"));
            }
        } else if (notReachEnd() && getNowChar() == '>') {
            curPos++;
            if (notReachEnd() && getNowChar() == '=') {
                nextChar();
                tokens.add(new SimpleToken(TokenType.GEQ, lineNum, ">="));
            } else {
                curPos--;
                nextChar();
                tokens.add(new SimpleToken(TokenType.GRE, lineNum, ">"));
            }
        } else if (notReachEnd() && getNowChar() == '=') {
            curPos++;
            if (notReachEnd() && getNowChar() == '=') {
                nextChar();
                tokens.add(new SimpleToken(TokenType.EQL, lineNum, "=="));
            } else {
                curPos--;
                nextChar();
                tokens.add(new SimpleToken(TokenType.ASSIGN, lineNum, "="));
            }
        } else if (notReachEnd() && getNowChar() == '!') {
            curPos++;
            if (notReachEnd() && getNowChar() == '=') {
                nextChar();
                tokens.add(new SimpleToken(TokenType.NEQ, lineNum, "!="));
            } else {
                curPos--;
                nextChar();
                tokens.add(new SimpleToken(TokenType.NOT, lineNum, "!"));
            }
        } else if (notReachEnd() && singleDelimiter.containsKey(getNowChar())) {
            tokens.add(new SimpleToken(singleDelimiter.get(getNowChar()), lineNum, String.valueOf(getNowChar())));
            nextChar();
        } else if (notReachEnd() && getNowChar() == '\n') {
            nextChar();
            lineNum++;
        } else if (notReachEnd()) {
            nextChar();
        }
    }

    private boolean isFSLegal(char c) {
        return c == 32 || c == 33 || (c >= 40 && c <= 126);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.toString());
        }
        return sb.toString();
    }
}


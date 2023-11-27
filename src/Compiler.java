import Backend.AsmBuilder;
import Frontend.Parser.CompUnit;
import Middle.CompilerError;
import Middle.Error.ErrorTable;
import Frontend.Lexer.Lexer;
import Frontend.Parser.Parser;
import Frontend.TokensReadControl;
import Middle.LLVMIR.IRBuilder;
import Middle.LLVMIR.IRModule;

import java.io.*;
import java.util.Scanner;

public class Compiler {
    public static void main(String[] args) throws IOException, CompilerError{
        boolean printLexer = false;
        boolean printParser = false;
        boolean errorCheck = true;
        boolean printLLVM = true;
        boolean printMips = true;
        InputStream stream2;
        try {
            stream2 = new FileInputStream("testfile.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(stream2);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
            sb.append('\n');
        }
        Lexer lexer = new Lexer(sb.toString());
        lexer.run();
        Parser parser = new Parser(new TokensReadControl(lexer.getTokens()));
        parser.parse();
        CompUnit cu = parser.getCompUnit();
        cu.generateIR();
        //System.out.println(IRBuilder.getInstance().getCurModule());
        File file1 = new File("output.txt");
        if(!file1.exists()) {
            file1.createNewFile();
        }
        File file2 = new File("error.txt");
        if(!file2.exists()) {
            file2.createNewFile();
        }
        File file3 = new File("error.txt");
        if(!file3.exists()) {
            file3.createNewFile();
        }
        File file4 = new File("mips.txt");
        if(!file4.exists()) {
            file4.createNewFile();
        }
        PrintWriter pw1 = new PrintWriter(new FileWriter("output.txt"));
        PrintWriter pw2 = new PrintWriter(new FileWriter("error.txt"));
        PrintWriter pw3 = new PrintWriter(new FileWriter("llvm_ir.txt"));
        PrintWriter pw4 = new PrintWriter(new FileWriter("mips.txt"));
        if(printLexer) {
            pw1.print(lexer);
        }
        if(printParser) {
            pw1.print(parser);
        }
        if(errorCheck) {
            parser.checkError();
            if(ErrorTable.getInstance().toString().isEmpty()) {
                if(printLLVM) {
                    pw3.print(IRBuilder.getInstance().getCurModule());
                }
                if(printMips) {
                    IRBuilder.getInstance().getCurModule().generateAsm();
                    pw4.print(AsmBuilder.getInstance());
                }
            }
        } else {
            if(printLLVM) {
                pw3.print(IRBuilder.getInstance().getCurModule());
            }
            if(printMips) {
                IRBuilder.getInstance().getCurModule().generateAsm();
                pw4.print(AsmBuilder.getInstance());
            }
        }
        pw1.close();
        pw2.close();
        pw3.close();
        pw4.close();
    }
}

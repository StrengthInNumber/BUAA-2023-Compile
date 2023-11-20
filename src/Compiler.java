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
        int outputType = 0;
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
        PrintWriter pw1 = new PrintWriter(new FileWriter("output.txt"));
        PrintWriter pw2 = new PrintWriter(new FileWriter("error.txt"));
        PrintWriter pw3 = new PrintWriter(new FileWriter("llvm_ir.txt"));
        //pw1.print(lexer);
        //pw1.print(parser);
        pw3.print(IRBuilder.getInstance().getCurModule());
        //parser.checkError();
        //pw2.print(ErrorTable.getInstance());
        pw1.close();
        pw2.close();
        pw3.close();
    }
}

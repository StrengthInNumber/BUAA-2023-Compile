import Check.CompilerError;
import Frontend.Lexer.Lexer;
import Frontend.Parser.Parser;
import Frontend.TokensReadControl;

import java.io.*;
import java.util.Scanner;

public class Compiler {
    public static void main(String[] args) throws IOException, CompilerError{
        InputStream stream1 = System.in;
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
        //System.out.print(sb);
        Lexer lexer = new Lexer(sb.toString());
        lexer.run();
        Parser parser = new Parser(new TokensReadControl(lexer.getTokens()));
        parser.parse();
        File file = new File("output.txt");
        if(!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));
        //pw.print(lexer);
        pw.print(parser);
        pw.close();
    }
}

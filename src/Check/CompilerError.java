package Check;

public class CompilerError extends Exception{
    public CompilerError(ErrorType type, int lineNum){
        System.out.println("in line " + lineNum + " we find " + type);
    }
}
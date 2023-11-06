package Check.Error;

public class Error implements Comparable<Error>{
    private final ErrorType type;
    private final int lineNum;

    public Error(int lineNum, ErrorType type){
        this.lineNum = lineNum;
        this.type = type;
    }

    @Override
    public int compareTo(Error o) {
        return Integer.compare(this.lineNum, o.lineNum);
    }

    @Override
    public String toString() {
        String sb = String.valueOf(lineNum) +
                ' ' +
                type.getCode();
        return sb;
    }
}

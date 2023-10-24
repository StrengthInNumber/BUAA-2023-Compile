package Check.Error;

public class Error implements Comparable<Error>{
    private ErrorType type;
    private int lineNum;

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
        StringBuilder sb = new StringBuilder();
        sb.append(lineNum);
        sb.append(' ');
        sb.append(type.getCode());
        return sb.toString();
    }
}

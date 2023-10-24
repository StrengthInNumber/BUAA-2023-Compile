package Check.Error;

import java.util.TreeSet;

public class ErrorTable {
    private static final ErrorTable ERROR_TABLE = new ErrorTable();
    private TreeSet<Error> errors = new TreeSet<>();

    public static ErrorTable getInstance() {
        return ERROR_TABLE;
    }

    public void addError(Error e) {
        errors.add(e);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Error e : errors) {
            sb.append(e);
            sb.append('\n');
        }
        return sb.toString();
    }
}

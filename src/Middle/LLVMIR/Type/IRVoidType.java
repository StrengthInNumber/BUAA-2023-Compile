package Middle.LLVMIR.Type;

public class IRVoidType extends IRType {
    public static IRVoidType VOID = new IRVoidType();
    private IRVoidType() {}

    public String toString() {
        return "void";
    }
}

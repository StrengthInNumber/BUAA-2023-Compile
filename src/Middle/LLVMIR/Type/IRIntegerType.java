package Middle.LLVMIR.Type;

public class IRIntegerType extends IRType {
    public static final IRIntegerType INT32 = new IRIntegerType(32);
    public static final IRIntegerType INT8 = new IRIntegerType(8);
    public static final IRIntegerType INT1 = new IRIntegerType(1);
    private int bitWidth; //0-void 1-i1 8-i8 32-i32

    public IRIntegerType(int bitWidth) {
        this.bitWidth = bitWidth;
    }

    public String toString() {
        if (this.equals(INT32)) {
            return "i32";
        } else if(this.equals(INT8)) {
            return "i8";
        } else if (this.equals(INT1)) {
            return "i1";
        } else {
            System.out.println("wrong in IRIntegerType.toString");
            return "fuck";
        }
    }

    public int getEleNum(){
        return 1;
    }
}

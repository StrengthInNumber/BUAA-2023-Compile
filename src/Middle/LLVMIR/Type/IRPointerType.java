package Middle.LLVMIR.Type;

public class IRPointerType extends IRType {
    private IRType contentType; //指向内容的类型

    public IRPointerType(IRType contentType){
        this.contentType = contentType;
    }

    public IRType getContentType() {
        return contentType;
    }

    public String toString() {
        return contentType.toString() + '*';
    }
}

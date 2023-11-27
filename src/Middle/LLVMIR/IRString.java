package Middle.LLVMIR;

import Backend.InstrAsm.AsmInstrAsciiz;

public class IRString {
    private String name;
    private String content;
    public IRString(String name) {
        this.name = name;
        content = "";
        IRBuilder.getInstance().addIRString(this);
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void addChar(char c) {
        content += c;
    }

    public void generateAsm() {
        new AsmInstrAsciiz(name, content);
    }

    public boolean isFirstChar() {
        return content.length() == 1 || content.equals("\\n");
    }
}

package Backend.InstrAsm;

public class AsmInstrAsciiz extends AsmInstr {
    private String name;
    private String content;

    public AsmInstrAsciiz(String name, String content) {
        super(AsmInstrOp.ASCIIZ);
        this.name = name;
        this.content = content.replace("\n", "\\n");
    }

    @Override
    public String toString() {
        return name + ": .asciiz \"" + content + "\"";
    }

}

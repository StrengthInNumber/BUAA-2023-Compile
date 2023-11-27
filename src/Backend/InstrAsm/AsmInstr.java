package Backend.InstrAsm;

import Backend.AsmBuilder;

public class AsmInstr {
    protected AsmInstrOp op;
    public AsmInstr(AsmInstrOp op, int mode){
        this.op = op;
        if(mode == 1) {
            AsmBuilder.getInstance().addDataInstr(this);
        } else if(mode == 2) {
            AsmBuilder.getInstance().addTextInstr(this);
        }
    }
}

package models.functions;

import models.components.VM;

public class Not extends Mnemonic {

    public Not() {
        super();
    }

    @Override
    public void execute(int typeB, int B) throws Exception {
        VM vm = VM.getInstance();

        int value = vm.dataReadHandler(B, typeB);
        vm.dataWriteHandler(B, ~value, typeB);
    }
}

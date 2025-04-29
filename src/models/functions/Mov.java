package models.functions;

import models.components.VM;

public class Mov extends Mnemonic {

    public Mov() {
        super();
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) throws Exception {
        VM vm = VM.getInstance();

        int value = vm.dataReadHandler(B, typeB);
        vm.dataWriteHandler(A, value, typeA);
    }

}

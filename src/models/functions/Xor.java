package models.functions;

import models.components.VM;

public class Xor extends Mnemonic{
   public Xor(VM vm) {
        super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
        int AValue = vm.dataReadHandler(A, typeA);
        int BValue = vm.dataReadHandler(B, typeB);
        vm.dataWriteHandler(A, AValue ^ BValue, typeA);
    }
}

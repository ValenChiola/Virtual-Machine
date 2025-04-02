package models.functions;

import models.components.VM;

public class Add extends Mnemonic {

    public Add(VM vm) {
      super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
      int BValue = vm.dataReadHandler(B, typeB);
      int AValue = vm.dataReadHandler(A, typeA);
      vm.dataWriteHandler(A, AValue + BValue, typeA);
    }
}

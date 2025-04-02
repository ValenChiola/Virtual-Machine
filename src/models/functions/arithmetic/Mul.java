package models.functions.arithmetic;

import models.components.VM;

public class Mul extends Arithmetic {
    public Mul(VM vm) {
      super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
      int BValue = vm.dataReadHandler(B, typeB);
      int AValue = vm.dataReadHandler(A, typeA);
      int result = AValue * BValue;
      vm.dataWriteHandler(A, result, typeA);
      super.setCC(result);
    }
}

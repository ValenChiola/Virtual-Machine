package models.functions.arithmetic;

import models.components.VM;

public class Div extends Arithmetic {
    public Div(VM vm) {
      super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
      int BValue = vm.dataReadHandler(B, typeB);
      int AValue = vm.dataReadHandler(A, typeA);

      if (BValue == 0)
        throw new Error("Divided by 0");

      int result = AValue / BValue;
      vm.dataWriteHandler(A, result, typeA);
      super.setCC(result);
    }
}

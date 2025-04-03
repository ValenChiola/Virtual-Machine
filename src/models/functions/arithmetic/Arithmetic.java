package models.functions.arithmetic;

import models.components.VM;
import models.functions.Mnemonic;

public abstract class Arithmetic extends Mnemonic {
    public Arithmetic(VM vm) {
      super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
      int AValue = vm.dataReadHandler(A, typeA);
      int BValue = vm.dataReadHandler(B, typeB);
      int result = getResult(AValue, BValue);
      vm.dataWriteHandler(A, result, typeA);
      setCC(result);
    }

    protected abstract int getResult(int AValue, int BValue);

    private void setCC(int value) {
      int result = 0;

      if (value < 0)
        result = 1 << 1;

      if (value == 0)
        result |= 1;

      result <<= 30;

      vm.registers.get(8).setValue(result);
    }

}

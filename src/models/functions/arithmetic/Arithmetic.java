package models.functions.arithmetic;

import models.components.VM;
import models.functions.Mnemonic;

public abstract class Arithmetic extends Mnemonic {
  public Arithmetic(VM vm) {
    super(vm);
  }

  public void setCC(int value) {
    int result = 0;
    
    if (value < 0)
      result = 1 << 1;

    if (value == 0)
      result |= 1;

    result <<= 30;

    vm.registers.get(8).setValue(result);
  }

}

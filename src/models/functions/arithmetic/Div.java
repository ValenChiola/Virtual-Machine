package models.functions.arithmetic;

import models.components.VM;

public class Div extends Arithmetic {
  public Div(VM vm) {
    super(vm);
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    if (BValue == 0)
      throw new Error("Divided by 0");

    return AValue / BValue;
  }

  @Override
  protected void afterWrite(int AValue, int BValue, int result) {
    vm.registers.get(9).setValue(AValue % BValue); // Set in AC the modulo
  }
}

package models.functions.arithmetic;

import models.components.VM;

public class Add extends Arithmetic {
  public Add(VM vm) {
    super(vm);
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    return AValue + BValue;
  }
}

package models.functions.arithmetic;

import models.components.VM;

public class Cmp extends Arithmetic {
  public Cmp(VM vm) {
    super(vm);
    this.overWritesA = false;
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    return AValue - BValue;
  }

}

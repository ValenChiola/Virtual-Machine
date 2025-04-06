package models.functions.arithmetic;

import models.components.VM;

public class Mul extends Arithmetic {
  public Mul(VM vm) {
    super(vm);
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    return AValue * BValue;
  }
}

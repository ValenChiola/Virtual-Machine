package models.functions.arithmetic;

public class Add extends Arithmetic {
  public Add() {
    super();
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    return AValue + BValue;
  }
}

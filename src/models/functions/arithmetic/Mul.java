package models.functions.arithmetic;

public class Mul extends Arithmetic {
  public Mul() {
    super();
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    return AValue * BValue;
  }
}

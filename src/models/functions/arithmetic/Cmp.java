package models.functions.arithmetic;

public class Cmp extends Arithmetic {
  public Cmp() {
    super();
    this.overWritesA = false;
  }

  @Override
  protected int getResult(int AValue, int BValue) {
    return AValue - BValue;
  }

}

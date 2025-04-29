package models.functions.jumps;

public class JZ extends Jump {

  public JZ() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return ((CCValue >>> 30) & 0x1) == 1;
  }

}

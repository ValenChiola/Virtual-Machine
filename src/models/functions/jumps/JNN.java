package models.functions.jumps;

public class JNN extends Jump {

  public JNN() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return CCValue >>> 31 == 0;
  }

}

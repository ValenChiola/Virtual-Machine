package models.functions.jumps;

public class JN extends Jump {

  public JN() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return CCValue >>> 31 == 1;
  }

}

package models.functions.jumps;

public class JNP extends Jump {

  public JNP() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return ((CCValue >>> 31) & 0x1) == 1 || ((CCValue >>> 30) & 0x1) == 1;
  }

}

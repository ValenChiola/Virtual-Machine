package models.functions.jumps;

public class JNZ extends Jump {

  public JNZ() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return ((CCValue >>> 30) & 0x1) == 0;
  }

}

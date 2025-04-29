package models.functions.jumps;

public class JP extends Jump {

  public JP() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return ((CCValue >>> 31) & 0x1) == 0 && ((CCValue >>> 30) & 0x1) == 0;
  }

}

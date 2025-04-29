package models.functions.jumps;

public class JMP extends Jump {

  public JMP() {
    super();
  }

  @Override
  protected boolean matchesCondition(int CCValue) {
    return true;
  }

}


package models.functions;

public class Ret extends Mnemonic {

  public Ret() {
    super();
  }

  @Override
  public void execute() throws Exception {
    new Pop().execute(1, (0x50));
  }

}


package models.functions;

public class Ret extends Mnemonic {

  public Ret() {
    super();
  }

  @Override
  public void execute() throws Exception {
    int IpRegisterCode = 5;
    new Pop().execute(1, (IpRegisterCode << 4));
  }

}

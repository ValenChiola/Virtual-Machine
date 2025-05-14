
package models.functions;

import models.components.Register;
import models.components.VM;

public class Ret extends Mnemonic {

  public Ret() {
    super();
  }

  @Override
  public void execute() throws Exception {
    VM vm = VM.getInstance();
    Register IP = vm.registers.get(5);

    new Pop().execute(2, IP.getValue(3));
  }

}

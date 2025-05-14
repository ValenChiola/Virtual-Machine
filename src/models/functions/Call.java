package models.functions;

import models.components.Register;
import models.components.VM;
import models.functions.jumps.JMP;

public class Call extends Mnemonic {

  public Call() {
    super();
  }

  @Override
  public void execute(int typeB, int B) throws Exception {
    VM vm = VM.getInstance();
    Register IP = vm.registers.get(5);
    new Push().execute(2, IP.getValue(3));
    new JMP().execute(typeB, B);
  }

}

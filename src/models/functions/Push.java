package models.functions;

import models.components.Register;
import models.components.VM;

public class Push extends Mnemonic {

  public Push() {
    super();
  }

  @Override
  public void execute(int typeB, int B) throws Exception {
    VM vm = VM.getInstance();
    Register SP = vm.registers.get(6);
    Register SS = vm.registers.get(3);

    SP.setValue(SP.getValue() - 4);

    if (SP.getValue() < SS.getValue())
      throw new Exception("Stack overflow");
    vm.ram.setValue(SP.getValue(), vm.dataReadHandler(B, typeB));
  }

}

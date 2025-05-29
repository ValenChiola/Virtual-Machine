package models.functions;

import models.components.Register;
import models.components.VM;

public class Pop extends Mnemonic {

  public Pop() {
    super();
  }

  @Override
  public void execute(int typeB, int B) throws Exception {
    VM vm = VM.getInstance();
    Register SP = vm.registers.get(6);

    try {
      int value = vm.ram.getValue(SP.getValue());
      vm.dataWriteHandler(B, value, typeB);
      SP.setValue(SP.getValue() + 4);
    } catch (Exception e) {
      throw new Exception("Stack underflow");
    }
  }

}

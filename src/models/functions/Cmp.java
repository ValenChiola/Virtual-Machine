package models.functions;

import models.components.Register;
import models.components.VM;

public class Cmp extends Mnemonic {
  public Cmp(VM vm) {
    super(vm);
  }

  @Override
  public void execute(int typeA, int typeB, int A, int B) {
    int BValue = vm.dataReadHandler(B, typeB);
    int AValue = vm.dataReadHandler(A, typeA);
    
    Register CC = vm.registers.get(8);
    
    int result = AValue - BValue;

    if (result < 0)
      CC.setValue(0x80000000);

    if (result == 0)
      CC.setValue(0x0);

  }

}

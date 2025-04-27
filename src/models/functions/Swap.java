package models.functions;

import models.components.VM;

public class Swap extends Mnemonic {
  public Swap(VM vm) {
    super(vm);
  }

  @Override
  public void execute(int typeA, int typeB, int A, int B) throws Exception {
    if (typeB == 0 || typeB == 2)
      throw new Exception("Swap Error. B type incorrect");

    if (typeA == 0 || typeA == 2)
      throw new Exception("Swap Error. A type incorrect");

    int AValue = vm.dataReadHandler(A, typeA);
    int BValue = vm.dataReadHandler(B, typeB);

    vm.dataWriteHandler(A, BValue, typeA);
    vm.dataWriteHandler(B, AValue, typeB);
  }

}

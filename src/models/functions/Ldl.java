package models.functions;

import models.components.VM;

public class Ldl extends Mnemonic {

  public Ldl() {
    super();
  }

  @Override
  public void execute(int typeA, int typeB, int A, int B) throws Exception {
    VM vm = VM.getInstance();

    int BValue = vm.dataReadHandler(B, typeB);
    int AValue = vm.dataReadHandler(A, typeA);
    vm.dataWriteHandler(A, (AValue & 0xFFFF0000) | (BValue & 0xFFFF), typeA);
  }

}

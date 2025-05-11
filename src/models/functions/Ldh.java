package models.functions;

import models.components.VM;

public class Ldh extends Mnemonic {

  public Ldh() {
    super();
  }

  @Override
  public void execute(int typeA, int typeB, int A, int B) throws Exception {
    VM vm = VM.getInstance();

    int BValue = vm.dataReadHandler(B, typeB);
    int AValue = vm.dataReadHandler(A, typeA);
    vm.dataWriteHandler(A, (BValue & 0xFFFF) << 16 | (AValue & 0xFFFF), typeA);
  }

}

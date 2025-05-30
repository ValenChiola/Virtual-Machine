package models.functions;

import models.components.VM;

public class Rnd extends Mnemonic {

  public Rnd() {
    super();
  }

  @Override
  public void execute(int typeA, int typeB, int A, int B) throws Exception {
    VM vm = VM.getInstance();

    int BValue = vm.dataReadHandler(B, typeB);
    vm.dataWriteHandler(A, (int) (Math.random() + BValue), typeA);
  }

}

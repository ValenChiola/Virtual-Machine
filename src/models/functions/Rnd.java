package models.functions;

import models.components.VM;

public class Rnd extends Mnemonic {

  public Rnd(VM vm){
    super(vm);
  }
  
  @Override
  public void execute(int typeA, int typeB, int A, int B) {
    int BValue = vm.dataReadHandler(B, typeB);
    vm.dataWriteHandler(A, (int)(Math.random() + BValue), typeA);
  }

}

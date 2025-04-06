package models.functions;

import models.components.VM;

public class Ldh extends Mnemonic {
  
    public Ldh(VM vm){
      super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) throws Exception {
      int BValue = vm.dataReadHandler(B, typeB);
      int AValue = vm.dataReadHandler(A, typeA);
      vm.dataWriteHandler(A, (AValue & 0xFF) | (BValue & 0xFF00), typeA);
    }

}

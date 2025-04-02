package models.functions.arithmetic;

import models.components.VM;

public class Or extends Arithmetic{
   public Or(VM vm) {
        super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
        int AValue = vm.dataReadHandler(A, typeA);
        int BValue = vm.dataReadHandler(B, typeB);
        int result = AValue | BValue;
        vm.dataWriteHandler(A, result, typeA);
        super.setCC(result);
    }
}

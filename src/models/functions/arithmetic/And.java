package models.functions.arithmetic;

import models.components.VM;

public class And extends Arithmetic{
   public And(VM vm) {
        super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
        int AValue = vm.dataReadHandler(A, typeA);
        int BValue = vm.dataReadHandler(B, typeB);
        int result = AValue & BValue;
        vm.dataWriteHandler(A, result, typeA);
        super.setCC(result);
    }
}

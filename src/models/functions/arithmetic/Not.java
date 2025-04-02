package models.functions.arithmetic;
import models.components.VM;
import models.functions.Mnemonic;

public class Not extends Mnemonic {

    public Not(VM vm) {
        super(vm);
    }

    @Override
    public void execute(int typeB, int B) {
        int value = vm.dataReadHandler(B, typeB);
        vm.dataWriteHandler(B, ~value, typeB);
    }
}

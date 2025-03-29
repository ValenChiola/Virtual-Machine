package models;

public class Mov extends Mnemonic {

    public Mov(VM vm) {
        super(vm);
    }

    @Override
    public void execute(int typeA, int typeB, int A, int B) {
        int value = vm.dataReadHandler(B, typeB);
        vm.dataWriteHandler(A, value, typeA);
    }

}

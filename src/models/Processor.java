package models;

public class Processor {

    private VM vm;

    public Processor(VM vm) {
        this.vm = vm;
    }

    private int logicToPhysic(int logicAddress) {
        int segment = logicAddress >> 16;
        int offset = logicAddress & 0xFFFF;

        int DSBase = vm.ts.getBase(segment);
        int physicAddress = offset + DSBase;

        if (physicAddress < DSBase) // If I am in the CS
            throw new Error("Out of bounds!");

        int DSLimit = vm.ts.getLimit(segment); // If I Pass the memory limit
        if (physicAddress + vm.bytesToAccess > DSLimit)
            throw new Error("Out of bounds!");

        return physicAddress;
    }

    public int getValue(int logicAddress) {
        int physicAddress = logicToPhysic(logicAddress);
        return vm.ram.getValue(physicAddress);
    }

    public int setValue(int logicAddress) {
        int physicAddress = logicToPhysic(logicAddress);
        return vm.ram.setValue(physicAddress);
    }

}

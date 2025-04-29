package models.components;

public class Processor {

    public int logicToPhysic(int logicAddress) throws Exception {
        VM vm = VM.getInstance();

        int segment = logicAddress >> 16;
        int offset = logicAddress & 0xFFFF;

        int DSBase = vm.ts.getBase(segment);
        int physicAddress = offset + DSBase;

        if (physicAddress < DSBase) // If I am in the CS
            throw new Exception("Out of bounds!");

        int DSLimit = vm.ts.getLimit(); // If I Pass the memory limit
        if (physicAddress + vm.bytesToAccess > DSLimit)
            throw new Exception("Out of bounds!");

        return physicAddress;
    }

}

package models.components;

public class Processor {

    public int logicToPhysic(int logicAddress) throws Exception {
        VM vm = VM.getInstance();
        return logicToPhysic(logicAddress, vm.bytesToAccess);
    }

    public int logicToPhysic(int logicAddress, int bytesToAccess) throws Exception {
        VM vm = VM.getInstance();

        int segment = logicAddress >> 16;
        int offset = logicAddress & 0xFFFF;

        int base = vm.ts.getBase(segment);
        int physicAddress = offset + base;

        if (physicAddress < base)
            throw new Exception("Out of bounds! Lower limit reached!");

        int limit = vm.ts.getLimit(segment);

        if (physicAddress + bytesToAccess > limit)
            throw new Exception(
                    "Out of bounds! Segment limit reached! " + (physicAddress + bytesToAccess) + " > " + limit);

        return physicAddress;
    }
}

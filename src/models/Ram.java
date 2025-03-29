package models;

public class Ram {
    private int capacity = 16384; // in bytes
    private byte[] cs;
    private byte[] ds;
    VM vm;

    public Ram(VM vm) {
        this.vm = vm;
    }

    public void init(byte[] code) {
        int codeSize = code.length;
        cs = new byte[codeSize];
        ds = new byte[capacity - codeSize];

        for (int i = 0; i < codeSize; i++)
            cs[i] = code[i];
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDSValue(int logicAddress) {
        int data = 0;

        for (int i = 0; i < vm.bytesToAccess; i++)
            data = (data << 8) + ds[logicAddress + i];

        return data;
    }

    public void setDSValue(int logicAddress, int value) {
        for (int i = 0; i < vm.bytesToAccess; i++)
            this.ds[logicAddress + i] = (byte) (value >> (12 - 4 * i));
    }

    public int getValue(int physicAddress) {
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }

    public int setValue(int physicAddress) {
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }

}
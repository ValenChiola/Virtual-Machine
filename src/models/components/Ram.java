package models.components;

public class Ram {
    private int capacity; // in bytes
    private byte[] memory;

    public Ram(int capacity) {
        this.capacity = capacity;
        memory = new byte[capacity];
    }

    public Ram() {
        this(16384);
    }

    public void init(byte[] code) {
        int CS = VM.getInstance().ts.getBase(0);
        System.arraycopy(code, 0, memory, CS, code.length);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getValue(int logicAddress) throws Exception {
        return getValue(logicAddress, VM.getInstance().bytesToAccess);
    }

    public int getValue(int logicAddress, int bytesToRead) throws Exception {
        int physicAddress = VM.getInstance().processor.logicToPhysic(logicAddress);
        int data = 0;

        for (int i = 0; i < bytesToRead; i++)
            data = (data << 8) | (this.memory[physicAddress + i] & 0xFF);

        return data;
    }

    public void setValue(int logicAddress, int value) throws Exception {
        setValue(logicAddress, value, VM.getInstance().bytesToAccess);
    }

    public void setValue(int logicAddress, int value, int bytesToWrite) throws Exception {
        int physicAddress = VM.getInstance().processor.logicToPhysic(logicAddress);
        for (int i = bytesToWrite - 1; i >= 0; i--) {
            this.memory[physicAddress + i] = (byte) (value & 0xFF);
            value >>>= 8;
        }
    }
}
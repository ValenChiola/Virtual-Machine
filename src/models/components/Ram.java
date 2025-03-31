package models.components;

public class Ram {
    private int capacity = 16384; // in bytes
    private byte[] memory;
    VM vm;

    public Ram(VM vm) {
        this.vm = vm;
        memory = new byte[capacity];
    }

    public void init(byte[] code) {
        int CS = vm.ts.getBase(vm.ts.getCSKey());
        System.arraycopy(code, 0, memory, CS, code.length);

        // for (int i = CS; i < capacity; i += 4)
        //     this.memory[i] = 1;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getValue(int logicAddress) {
        int physicAddress = vm.processor.logicToPhysic(logicAddress);
        int data = 0;
        
        for (int i = 0; i < vm.bytesToAccess; i++){
            data = (data << 8) + this.memory[physicAddress + i];
        }
        return data;
    }

    public void setValue(int logicAddress, int value) {
        setValue(logicAddress, value, vm.bytesToAccess)
    }

    public void setValue(int logicAddress, int value, int bytesToWrite) {
        int physicAddress = vm.processor.logicToPhysic(logicAddress);

        for (int i = bytesToWrite; i >= 0; i--){ 
            this.memory[physicAddress + i] = value & 0xFF;
            value >>= 8;
        }
    }
}
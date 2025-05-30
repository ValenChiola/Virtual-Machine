package models.components;

import java.util.List;
import utils.ArgsParser;

public class Ram {
    private int capacity; // in bytes
    private byte[] memory;

    public Ram(int capacity) {
        this.capacity = capacity;
        memory = new byte[capacity];
    }

    public void init() throws Exception {
        VM vm = VM.getInstance();

        int csBase = vm.ts.getBase(vm.ts.cs);
        int ksBase = vm.ts.getBase(vm.ts.ks);

        int psSize = vm.ts.getSize(vm.ts.ps);
        int ksSize = vm.ts.getSize(vm.ts.ks);
        int csSize = vm.ts.getSize(vm.ts.cs);
        int dsSize = vm.ts.getSize(vm.ts.ds);
        int ssSize = vm.ts.getSize(vm.ts.ss);
        int esSize = vm.ts.getSize(vm.ts.es);

        int requestedSize = psSize + ksSize + csSize + dsSize + ssSize + esSize;

        if (requestedSize > capacity)
            throw new Exception(
                    "Not enough memory - Requested: " + requestedSize + " bytes, Available: " + capacity + " bytes");
        if (vm.version == 2) {
            // copy params to RAM
            List<String> params = ArgsParser.getProgramParams();

            int i = 0;
            int paramCounter = 0;
            int initPointerIndex = psSize - 4 * params.size();

            for (String param : params) {
                try {
                    setValue(initPointerIndex + paramCounter * 4, i);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                paramCounter++;
                for (int j = 0; j < param.length(); j++)
                    memory[i++] = (byte) param.charAt(j);

                memory[i++] = 0;
            }

            // copy constant data to RAM
            if (ksSize > 0)
                System.arraycopy(vm.constants, 0, memory, ksBase, ksSize);

        }
        // copy code to RAM
        System.arraycopy(vm.code, 0, memory, csBase, csSize);
    }

    public int getCapacity() {
        return capacity;
    }

    public byte[] getMemory() {
        return memory;
    }

    public int getValue(int logicAddress) throws Exception {
        return getValue(logicAddress, VM.getInstance().bytesToAccess);
    }

    public int getValue(int logicAddress, int bytesToRead) throws Exception {
        int physicAddress = VM.getInstance().processor.logicToPhysic(logicAddress, bytesToRead);
        int data = 0;

        for (int i = 0; i < bytesToRead; i++)
            data = (data << 8) | (this.memory[physicAddress + i] & 0xFF);

        return (data << (32 - (bytesToRead * 8)) >> (32 - (bytesToRead * 8)));
    }

    public void setValue(int logicAddress, int value) throws Exception {
        setValue(logicAddress, value, VM.getInstance().bytesToAccess);
    }

    public void setValue(int logicAddress, int value, int bytesToWrite) throws Exception {
        int physicAddress = VM.getInstance().processor.logicToPhysic(logicAddress, bytesToWrite);
        for (int i = bytesToWrite - 1; i >= 0; i--) {
            this.memory[physicAddress + i] = (byte) (value & 0xFF);
            value >>>= 8;
        }
    }
}

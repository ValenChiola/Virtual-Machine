package models.components;

import java.util.List;
import utils.Converter;
import utils.ArgsParser;

public class Ram {
    private int capacity; // in bytes
    private byte[] memory;

    public Ram(int capacity) {
        this.capacity = capacity;
        memory = new byte[capacity];
    }

    public void init() {
        VM vm = VM.getInstance();
        int csBase = vm.ts.getBase(vm.ts.cs);
        int csSize = vm.ts.getSize(vm.ts.cs);

        // copy params to RAM
        List<String> params = ArgsParser.getProgramParams();
        int psSize = vm.ts.getSize(vm.ts.ps);

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

        return data;
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

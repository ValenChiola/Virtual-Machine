package models.components;

import java.util.Map;
import java.util.HashMap;

public class TableSegments {

    private VM vm;
    private Map<Integer, Integer[]> table;

    public TableSegments(VM vm) {
        this.vm = vm;
        table = new HashMap<>();
    }

    public void init(byte[] code) {
        table.put(0, new Integer[] { 0, code.length });
        table.put(1, new Integer[] { code.length, vm.ram.getCapacity() - code.length });
    }

    public int getBase(int segment) {
        return table.get(segment)[0];
    }

    public int getBaseShifted(int segment) {
        return getBase(segment) << 16;
    }

    public int getSize(int segment) {
        return table.get(segment)[1];
    }

    public int getLimit() {
        return getBase(1) + getSize(1);
    }

}
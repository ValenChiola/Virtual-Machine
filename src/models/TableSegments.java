package models;

import java.util.Map;
import java.util.HashMap;

public class TableSegments {

    private VM vm;
    private Map<Integer, Integer[]> table;
    private int cs = 0;
    private int ds = 1;

    public TableSegments(VM vm) {
        this.vm = vm;
        table = new HashMap<>();
    }

    public void init(byte[] code) {
        table.put(cs, new Integer[] { 0, code.length });
        table.put(ds, new Integer[] { code.length, vm.ram.getCapacity() - code.length });
    }

    public int getBase(int segment) {
        return table.get(segment)[0];
    }

    public int getSize(int segment) {
        return table.get(segment)[1];
    }

    public int getCSKey() {
        return cs;
    }

    public int getDSKey() {
        return ds;
    }

    public int getLimit() {
        return getBase(ds) + getSize(ds);
    }

}
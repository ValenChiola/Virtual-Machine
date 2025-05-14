package models.components;

import java.util.HashMap;
import java.util.Map;
import utils.log.Log;
import utils.ArgsParser;
import java.util.List;

public class TableSegments {

    private Map<Integer, Integer[]> table;
    public int ps;
    public int ks;
    public int cs;
    public int ds = 1;
    public int es;
    public int ss;

    public TableSegments() {
        table = new HashMap<>();
    }

    public void init() {
        VM vm = VM.getInstance();
        if (vm.version == 1) {
            table.put(cs, new Integer[] { 0, vm.code.length });
            table.put(ds, new Integer[] { vm.code.length, vm.ram.getCapacity() - vm.code.length });
        } else if (vm.version == 2) {
            int index = 0;
            int totalSize = 0;
            List<String> params = ArgsParser.getProgramParams();
            Log.debug("----------------Table Segments----------------");
            if (params.size() > 0) {
                int psSize = 0;

                for (String param : params)
                    psSize += param.length() + 1; // Longitud de cada param + \0

                psSize += params.size() * 4; // Los punteros contiguos (4 bytes c/u)

                ps = index;
                table.put(ps, new Integer[] { totalSize, psSize });
                index++;
                Log.debug("PS - Segment: " + ps + " Base: " + totalSize + " Size: " + psSize);
                totalSize += psSize;
            }
            if (vm.ksSize > 0) {
                ks = index;
                table.put(ks, new Integer[] { totalSize, vm.ksSize });
                index++;
                Log.debug("KS - Segment: " + ks + " Base: " + totalSize + " Size: " + vm.ksSize);
                totalSize += vm.ksSize;
            }

            cs = index;
            table.put(cs, new Integer[] { totalSize, vm.code.length });
            index++;
            Log.debug("CS - Segment: " + cs + " Base: " + totalSize + " Size: " + vm.code.length);
            totalSize += vm.code.length;
            if (vm.dsSize > 0) {
                ds = index;
                table.put(ds, new Integer[] { totalSize, vm.dsSize });
                index++;
                Log.debug("DS - Segment: " + ds + " Base: " + totalSize + " Size: " + vm.dsSize);
                totalSize += vm.dsSize;
            }
            if (vm.esSize > 0) {
                es = index;
                table.put(es, new Integer[] { totalSize, vm.esSize });
                index++;
                Log.debug("ES - Segment: " + es + " Base: " + totalSize + " Size: " + vm.esSize);
                totalSize += vm.esSize;
            }
            if (vm.ssSize > 0) {
                ss = index;
                table.put(ss, new Integer[] { totalSize, vm.ssSize });
                index++;
                Log.debug("SS - Segment: " + ss + " Base: " + totalSize + " Size: " + vm.ssSize);
                totalSize += vm.ssSize;
            }
        }
        Log.debug("----------------------------------------------");
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

    public int getLimit(int segment) {
        return getBase(segment) + getSize(segment);
    }

    public int getSize() {
        return table.size();
    }

    public int getValue(int segment) {
        if (!table.containsKey(segment))
            return 0;
        return getBaseShifted(segment) | getSize(segment);
    }

    public void setValue(int segment, int base, int size) {
        table.put(segment, new Integer[] { base, size });
    }

}

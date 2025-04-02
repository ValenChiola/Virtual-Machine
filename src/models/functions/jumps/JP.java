package models.functions.jumps;

import models.components.Register;
import models.components.VM;
import models.functions.Mnemonic;

public class JP extends Mnemonic {

    public JP(VM vm) {
      super(vm);
    }

    @Override
    public void execute(int typeB, int B) {
      int value = vm.dataReadHandler(B, typeB);

      Register CC = vm.registers.get(8);
      Register IP = vm.registers.get(5);

      if (((CC.getValue() >>> 31) & 0x1) == 0)
        IP.setValue(0x00000000 + value);
    }

}

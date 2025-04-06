package models.functions.jumps;

import models.components.Register;
import models.components.VM;
import models.functions.Mnemonic;

public class JMP extends Mnemonic {
    
    public JMP(VM vm) {
      super(vm);
    }

    @Override
    public void execute(int typeB, int B) throws Exception {
      int value = vm.dataReadHandler(B, typeB);
      Register IP = vm.registers.get(5);
      IP.setValue(0x00000000 + value);
    }

}

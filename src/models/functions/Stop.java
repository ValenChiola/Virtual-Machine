package models.functions;

import models.components.Register;
import models.components.VM;

public class Stop extends Mnemonic {

    public Stop(VM vm) {
        super(vm);
    }

    @Override
    public void execute() {
       Register IP = vm.registers.get(5);
       IP.setValue(0x00000000 + vm.ts.getSize(0));
    }
    
}

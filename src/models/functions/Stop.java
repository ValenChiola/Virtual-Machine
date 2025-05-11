package models.functions;

import models.components.Register;
import models.components.VM;

public class Stop extends Mnemonic {

    public Stop() {
        super();
    }

    @Override
    public void execute() {
        VM vm = VM.getInstance();

        Register IP = vm.registers.get(5);
        IP.setValue(vm.ts.cs << 16 | vm.ts.getSize(0));
    }

}

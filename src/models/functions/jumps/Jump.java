package models.functions.jumps;

import models.components.Register;
import models.components.VM;
import models.functions.Mnemonic;

public abstract class Jump extends Mnemonic {

    public Jump() {
        super();
    }

    @Override
    public void execute(int typeB, int B) throws Exception {
        VM vm = VM.getInstance();

        int value = vm.dataReadHandler(B, typeB);

        Register CC = vm.registers.get(8);
        Register IP = vm.registers.get(5);

        if (matchesCondition(CC.getValue()))
            IP.setValue(vm.ts.cs << 16 | value);
    }

    protected abstract boolean matchesCondition(int CCValue);

}

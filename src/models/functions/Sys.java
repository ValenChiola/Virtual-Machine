package models.functions;

import java.util.Scanner;

import models.components.Register;
import models.components.VM;
import utils.Converter;

public class Sys extends Mnemonic {

    Scanner sc;

    public Sys() {
        super();
        sc = new Scanner(System.in);
    }

    @Override
    public void execute(int typeB, int B) throws Exception {
        VM vm = VM.getInstance();

        int code = vm.dataReadHandler(B, typeB);

        Register EDX = vm.registers.get(13);
        Register ECX = vm.registers.get(12);
        Register EAX = vm.registers.get(10);
        int CH = ECX.getValue(2);
        int CL = ECX.getValue(1);
        int AL = EAX.getValue(1);

        if (code == 1) { // Read
            for (int i = 0; i < CL; i++) {
                System.out.print(">>> ");
                int data = Converter.stringToNumber(sc.nextLine(), AL);
                int logicAddress = EDX.getValue() + i * CH;
                vm.ram.setValue(logicAddress, data, CH);
            }
        } else if (code == 2) { // Write
            for (int i = 0; i < CL; i++) {
                int logicAddress = EDX.getValue() + i * CH;
                int value = vm.ram.getValue(logicAddress, CH);
                System.out.println("[" + String.format("%04X", vm.processor.logicToPhysic(logicAddress)) + "]" + ": "
                        + Converter.numberToString(value, AL, CH));
            }
        }
    }
}

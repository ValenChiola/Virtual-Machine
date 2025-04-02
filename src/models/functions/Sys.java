package models.functions;

import java.util.Scanner;

import models.components.Register;
import models.components.VM;
import utils.Converter;

public class Sys extends Mnemonic {

    Scanner sc;

    public Sys(VM vm) {
        super(vm);
        sc = new Scanner(System.in);
    }

    @Override
    public void execute(int typeB, int B) {
        int code = vm.dataReadHandler(B, typeB);

        Register EDX = vm.registers.get(13);
        Register ECX = vm.registers.get(12);
        Register EAX = vm.registers.get(10);
        int CH = ECX.getValue(2);
        int CL = ECX.getValue(1);
        int AL = EAX.getValue(1);

        if (code == 1) { // Leer
            for (int i = 0; i < CL; i++) {
                int fullData = Converter.stringToInt(sc.nextLine(), AL);

                for (int j = 0; j < CH; j++){
                    int data = (fullData >> ((CH - (j + 1)) * 8)) & 0xFF;
                    int logicAddress = EDX.getValue() + i * CL + j;
                    vm.ram.setValue(logicAddress, data, 1);
                }
            }
        } else if (code == 2) { // Write
            // for (int i = 0; i < CL; i++) { 
            //     int fullData = EDX.getValue();
            //     int data = 0;

            //     for (int j = 0; j < CH; j++)
            //         data |= ((fullData >> (8 * j)) & 0xFF) << (8 * j);
            // }
        }

    }
}

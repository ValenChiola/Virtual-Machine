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

        if (code == 1) {
            for (int i = 0; i < CL; i++) { // TODO: Chequear con juan el tema del CL
                int fullData = Converter.stringToInt(sc.nextLine(), AL);
                int data = 0;

                for (int j = 0; j < CH; j++)
                    data |= ((fullData >> (8 * j)) & 0xFF) << (8 * j);
                
                vm.dataWriteHandler(0xD0, data, 1);
                //vm.dataWriteHandler(EDX.getValue() + (i * CL), data, 3);
                
            }
        } else if (code == 2) {
            for (int i = 0; i < CL; i++) { 
                int fullData = EDX.getValue();
                int data = 0;

                for (int j = 0; j < CH; j++)
                    data |= ((fullData >> (8 * j)) & 0xFF) << (8 * j);

                System.out.println(data);
            }
        }

    }
}

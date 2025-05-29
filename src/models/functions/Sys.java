package models.functions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import models.components.Register;
import models.components.VM;
import utils.ArgsParser;
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

        int CX = ECX.getValue(3);
        int CH = ECX.getValue(2);
        int CL = ECX.getValue(1);
        int AL = EAX.getValue(1);

        if (code == 1) { // Read
            for (int i = 0; i < CL; i++) {
                int logicAddress = EDX.getValue() + i * CH;
                System.out.print("[" + String.format("%04X", vm.processor.logicToPhysic(logicAddress, CH)) + "]: ");
                int data = Converter.stringToNumber(sc.nextLine(), AL);
                vm.ram.setValue(logicAddress, data, CH);
            }
        } else if (code == 2) { // Write
            for (int i = 0; i < CL; i++) {
                int logicAddress = EDX.getValue() + i * CH;
                int value = vm.ram.getValue(logicAddress, CH);
                System.out.println(
                        "[" + String.format("%04X", vm.processor.logicToPhysic(logicAddress, CH)) + "]" + ": "
                                + Converter.numberToString(value, AL, CH));
            }
        } else if (code == 3) {

            String data = sc.nextLine();

            if (CX == -1)
                CX = data.length();

            int i = 0;
            while (i < CX && i < data.length()) {
                int logicAddress = EDX.getValue() + i;
                vm.ram.setValue(logicAddress, data.charAt(i), 1);
                i++;
            }

            vm.ram.setValue(EDX.getValue() + i, 0, 1); // \0

        } else if (code == 4) {
            int i = 0;

            int lastByte = 0;
            do {
                int logicAddress = EDX.getValue() + i;
                lastByte = vm.ram.getValue(logicAddress, 1);

                if (lastByte != 0) {
                    if (lastByte == 10)
                        System.out.println();
                    else
                        System.out.print(Converter.numberToString(lastByte, 0x02, 1));
                }
                i++;
            } while (lastByte != 0);

        } else if (code == 7) {
            // Clear Screen
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } else if (code == 0xF) {
            String vmiFile = ArgsParser.getVmiFile();
            if (vmiFile == null)
                return;
            createVmiFile(vmiFile);
            // Pedir accion
            String action = sc.nextLine();
            switch (action.toLowerCase()) {
                case "g":
                    break;
                case "q":
                    System.exit(0);
                    break;
                default:
                    vm.executeNextOperation();
                    new Sys().execute(typeB, B);
                    break;
            }
        }
    }

    private void createVmiFile(String vmiFile) {
        VM vm = VM.getInstance();
        int ramCapacity = vm.ram.getCapacity();
        int vmiLength = 8 + 64 + 32 + ramCapacity;
        byte[] data = new byte[vmiLength];

        // Escribir Identificador
        byte[] header = "VMI25".getBytes();
        System.arraycopy(header, 0, data, 0, header.length);
        data[5] = (byte) vm.version;
        data[6] = (byte) ((ramCapacity >>> 8) & 0xFF);
        data[7] = (byte) (ramCapacity & 0xFF);

        // Escribir registros
        for (int i = 0; i < 16; i++) {
            int value = vm.registers.get(i).getValue();
            data[8 + i * 4] = (byte) ((value >>> 24) & 0xFF);
            data[9 + i * 4] = (byte) ((value >>> 16) & 0xFF);
            data[10 + i * 4] = (byte) ((value >>> 8) & 0xFF);
            data[11 + i * 4] = (byte) (value & 0xFF);
        }

        // Escribir tabla de segmentos
        for (int i = 0; i < 8; i++) {
            int value = vm.ts.getValue(i);
            data[64 + i * 4] = (byte) ((value >>> 24) & 0xFF);
            data[65 + i * 4] = (byte) ((value >>> 16) & 0xFF);
            data[66 + i * 4] = (byte) ((value >>> 8) & 0xFF);
            data[67 + i * 4] = (byte) (value & 0xFF);
        }

        // Escribir la memoria
        System.arraycopy(vm.ram.getMemory(), 0, data, 104, ramCapacity);

        try {
            Files.write(Path.of(vmiFile), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

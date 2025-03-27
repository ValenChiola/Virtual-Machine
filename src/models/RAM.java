package models;

public class RAM {
    private int capacity = 16384; // in bytes
    private byte[] cs;
    private byte[] ds;

    public void init(byte[] code) {
        int codeSize = code.length;
        cs = new byte[codeSize];
        ds = new byte[capacity - codeSize];

        System.out.println(codeSize);

        for (int i = 0; i < codeSize; i++)
            cs[i] = code[i];

        for (int i = 0; i < codeSize; i++) {
            System.out.printf("0x%X", cs[i]); // fase de prueba
            System.out.print(" "); // fase de prueba
            
        }

    }

    public int getValue(int physicAddress) {
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }

    public int setValue(int physicAddress) {
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }

}
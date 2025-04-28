package utils;

public class Converter {

    public static int stringToNumber(String data, int AL) {
        if (AL == 0x01 || AL == 0x02) // Decimal o Caracteres
            return Integer.parseInt(data, 10);
        if (AL == 0x04)
            return Integer.parseInt(data, 8); // Octal
        if (AL == 0x08)
            return Integer.parseInt(data, 16); // Hexadecimal
        if (AL == 0x10)
            return Integer.parseInt(data, 2); // Binario

        throw new Error("Converter failed: no valid mode in AL");
    }

    public static String numberToString(int data, int AL) {
        String result = "";

        if ((AL & 0x10) != 0) // Binario
            result += "0b" + Integer.toBinaryString(data).replace(' ', '0') + " ";

        if ((AL & 0x08) != 0) // Hexadecimal
            result += "0x" + Integer.toHexString(data).toUpperCase().replace(' ', '0') + " ";

        if ((AL & 0x04) != 0) // Octal
            result += "0o" + Integer.toOctalString(data).replace(' ', '0') + " ";

        if ((AL & 0x02) != 0) { // Caracteres
            char c1 = (char) (data & 0xFF);
            char c2 = (char) ((data >> 8) & 0xFF);
            char c3 = (char) ((data >> 16) & 0xFF);
            char c4 = (char) ((data >> 24) & 0xFF);

            if (c1 <= 31 || c1 >= 127)
                c1 = '.';
            if (c2 <= 31 || c2 >= 127)
                c2 = '.';
            if (c3 <= 31 || c3 >= 127)
                c3 = '.';
            if (c4 <= 31 || c4 >= 127)
                c4 = '.';

            result += "" + c4 + c3 + c2 + c1 + " ";
        }

        if ((AL & 0x01) != 0) // Decimal
            result += data + " ";

        if (result.isEmpty())
            throw new Error("Converter failed: no valid mode in AL");

        return result.trim();
    }
}

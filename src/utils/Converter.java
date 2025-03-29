package utils;

public class Converter {

    public static byte hexaToBinary(String hex) {
        if (hex == null || hex.length() != 2)
            throw new IllegalArgumentException("El valor hexadecimal debe tener exactamente 2 caracteres.");

        return (byte) Integer.parseInt(hex, 16);
    }
}

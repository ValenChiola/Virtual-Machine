package utils;

public class Converter {

    public static int stringToInt(String data, int AL) {
        if (AL == 0x01 || AL == 0x02) return Integer.parseInt(data, 10);
        if (AL  == 0x04) return Integer.parseInt(data, 8);
        if (AL  == 0x08) return Integer.parseInt(data, 16);
        if (AL  == 0x10) return Integer.parseInt(data, 2);
    
        throw new Error("Converter failed");
    }
}

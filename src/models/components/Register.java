package models.components;

public class Register {
    private String name;
    private int value;

    public Register(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Register(String name) {
        this(name, 0);
    }

    public Register() {
        this("Unknown", 0);
    }

    public int getValue() {
        return this.value;
    }

    public int getValue(int identifier) {
        int value = getValue(); // Obtener el valor completo

        if (identifier == 0) {
            return value; // Retornar el valor completo
        } else if (identifier == 1) { // AL (8 bits)
            int al = value & 0xFF;
            return (al << 24) >> 24; // Extender signo (convertirlo en int de 32 bits)
        } else if (identifier == 2) { // AH (8 bits)
            int ah = (value >> 8) & 0xFF; // Extraer AH
            return (ah << 24) >> 24; // Extender signo (convertirlo en int de 32 bits)
        } else { // AX (16 bits)
            int ax = value & 0xFFFF; // Extraer AX
            return (ax << 16) >> 16; // Extender signo (convertirlo en int de 32 bits)
        }
    }

    public String getName() {
        return this.name;
    }

    public String getName(int identifier) {
        if (identifier == 0 || this.name.length() < 3) return this.name; // Retornar el valor completo
        

        if (identifier == 1)  // AL (8 bits)
            return this.name.charAt(1) + "L"; // Extender signo (convertirlo en int de 32 bits)
        
        if (identifier == 2) // AH (8 bits)
            return this.name.charAt(1) + "H"; // Extender signo (convertirlo en int de 32 bits)
        
        // AX (16 bits)
        return this.name.substring(1);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(int value, int identifier) {
        if (identifier == 0)
            this.value = value;
        else if (identifier == 1) // AL
            this.value = (getValue() & 0xFFFFFF00) | (value & 0xFF);
        else if (identifier == 2) // AH
            this.value = (getValue() & 0xFFFF00FF) | ((value & 0xFF) << 8);
        else // AX
            this.value = (getValue() & 0xFFFF0000) | (value & 0xFFFF);
    }
}
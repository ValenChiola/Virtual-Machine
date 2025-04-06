package models.components;

public class Register {
    private int value;

    public Register(int value) {
        this.value = value;
    }

    public Register() {
        this(0);
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
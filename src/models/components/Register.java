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
        if (identifier == 0)
            return getValue();
        else if (identifier == 1) // AL
            return getValue() & 0xFF;
        else if (identifier == 2) // AH
            return (getValue() & 0xFF00) >> 8;
        else // AX
            return getValue() & 0xFFFF;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(int value, int identifier) {
        if (identifier == 0)
            this.value = value;
        else if (identifier == 1)
            // AL
            this.value = (getValue() & 0xFFFFFF00) | (value & 0xFF);
        else if (identifier == 2)
            // AH
           { this.value = (getValue() & 0xFFFF00FF) | ((value & 0xFF) << 8);
        }
        else
            // AX
            this.value = (getValue() & 0xFFFF0000) | (value & 0xFFFF);
    }
}
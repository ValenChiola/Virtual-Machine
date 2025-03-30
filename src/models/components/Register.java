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
            return this.value;
        else if (identifier == 1)
            // AL
            return this.value & 0xFF;
        else if (identifier == 2)
            // AH
            return (this.value & 0xFF00) >> 8;
        else
            // AX
            return this.value & 0xFFFF;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(int value, int identifier) {
        if (identifier == 0)
            this.value = value;
        else if (identifier == 1)
            // AL
            this.value = (this.value & 0xFFFFFF00) + value & 0xFF;
        else if (identifier == 2)
            // AH
            this.value = (this.value & 0xFFFF00FF) + value & 0xFF00;
        else
            // AX
            this.value = (this.value & 0xFFFF0000) + value & 0xFFFF;
    }
}
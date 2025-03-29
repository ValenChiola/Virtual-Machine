package models;

public class Register {
    private int value;

    public Register(int value) {
        this.value = value;
    }

    public Register() {
        this(0);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
package models.functions.arithmetic;

public class Xor extends Arithmetic {
    public Xor() {
        super();
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue ^ BValue;
    }
}

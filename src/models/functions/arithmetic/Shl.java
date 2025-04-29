package models.functions.arithmetic;

public class Shl extends Arithmetic {
    public Shl() {
        super();
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue << BValue;
    }
}

package models.functions.arithmetic;

public class Shr extends Arithmetic {
    public Shr() {
        super();
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue >> BValue;
    }
}

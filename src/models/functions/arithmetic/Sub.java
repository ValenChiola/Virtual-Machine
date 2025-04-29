package models.functions.arithmetic;

public class Sub extends Arithmetic {
    public Sub() {
        super();
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue - BValue;
    }
}

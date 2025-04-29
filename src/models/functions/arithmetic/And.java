package models.functions.arithmetic;

public class And extends Arithmetic {
    public And() {
        super();
    }

    @Override
    public int getResult(int AValue, int BValue) {
        return AValue & BValue;
    }
}

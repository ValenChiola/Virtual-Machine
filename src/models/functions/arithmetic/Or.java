package models.functions.arithmetic;

public class Or extends Arithmetic {
    public Or() {
        super();
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue | BValue;
    }
}

package models.functions.arithmetic;

import models.components.VM;

public class Shr extends Arithmetic {
    public Shr(VM vm) {
        super(vm);
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue >> BValue;
    }
}

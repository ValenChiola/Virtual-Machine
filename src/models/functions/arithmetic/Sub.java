package models.functions.arithmetic;

import models.components.VM;

public class Sub extends Arithmetic {
    public Sub(VM vm) {
        super(vm);
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue - BValue;
    }
}

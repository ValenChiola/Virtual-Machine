package models.functions.arithmetic;

import models.components.VM;

public class Or extends Arithmetic {
    public Or(VM vm) {
        super(vm);
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue | BValue;
    }
}

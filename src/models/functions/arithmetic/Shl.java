package models.functions.arithmetic;

import models.components.VM;

public class Shl extends Arithmetic {
    public Shl(VM vm) {
        super(vm);
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue << BValue;
    }
}

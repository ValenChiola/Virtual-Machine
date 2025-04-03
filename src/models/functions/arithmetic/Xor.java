package models.functions.arithmetic;

import models.components.VM;

public class Xor extends Arithmetic{
    public Xor(VM vm) {
        super(vm);
    }

    @Override
    protected int getResult(int AValue, int BValue) {
        return AValue ^ BValue;
    }
}

package models.functions.arithmetic;

import models.components.VM;

public class And extends Arithmetic{
    public And(VM vm) {
        super(vm);
    }

    @Override
    public int getResult(int AValue, int BValue) {
       return AValue & BValue;
    }
}

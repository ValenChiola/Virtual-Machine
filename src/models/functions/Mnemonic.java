package models.functions;

import models.components.VM;

public abstract class Mnemonic {

    protected VM vm;;

    public Mnemonic(VM vm) {
        this.vm = vm;
    }

    public void _execute(int typeA, int typeB, int A, int B) {
        if (typeA == 0 && typeB == 0)
            execute();

        else if (typeA == 0)
            execute(typeB, B);

        else
            execute(typeA, typeB, A, B);
    }

    public void execute(int typeA, int typeB, int A, int B) {
        throw new IllegalArgumentException("Error de parámetros");
    }

    public void execute(int typeB, int B) {
        throw new IllegalArgumentException("Error de parámetros");
    }

    public void execute() {
        throw new IllegalArgumentException("Error de parámetros");
    }

}

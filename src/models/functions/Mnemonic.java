package models.functions;

public abstract class Mnemonic {

    protected String name;

    public Mnemonic() {
        this.name = this.getClass().getSimpleName().toUpperCase();
    }

    public void _execute(int typeA, int typeB, int A, int B) throws Exception {
        if (typeA == 0 && typeB == 0)
            execute();
        else if (typeA == 0)
            execute(typeB, B);
        else
            execute(typeA, typeB, A, B);
    }

    public void execute(int typeA, int typeB, int A, int B) throws Exception {
        throw new IllegalArgumentException("Error de parámetros");
    }

    public void execute(int typeB, int B) throws Exception {
        throw new IllegalArgumentException("Error de parámetros");
    }

    public void execute() throws Exception {
        throw new IllegalArgumentException("Error de parámetros");
    }

    public String getName() {
        return this.name;
    }

}

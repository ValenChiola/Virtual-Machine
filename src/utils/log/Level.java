package utils.log;

public enum Level {
    ERROR,
    INFO,
    WARN,
    DEBUG;

    // Método para obtener el Level desde un String (como JavaScript)
    public static Level fromString(String name) {
        for (Level level : Level.values()) {
            if (level.name().equalsIgnoreCase(name)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Nivel no válido: " + name);
    }

}

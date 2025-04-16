package utils.log;

public enum Level {
    ERROR("ERROR"),
    INFO("INFO"),
    WARN("WARN"),
    DIS("-d"),
    DEBUG("DEBUG");

    private final String display;

    Level(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public static Level fromString(String name) {
        for (Level level : Level.values()) {
            if (level.display.equalsIgnoreCase(name) || level.name().equalsIgnoreCase(name))
                return level;
        }

        throw new IllegalArgumentException("Nivel no válido: " + name);
    }
}

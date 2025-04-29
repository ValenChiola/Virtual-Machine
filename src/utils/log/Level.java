package utils.log;

public enum Level {
    ERROR,
    INFO,
    WARN,
    DEBUG;

    public static Level fromString(String name) {
        for (Level level : Level.values()) {
            if (level.name().equalsIgnoreCase(name))
                return level;
        }

        return Level.INFO;
    }
}

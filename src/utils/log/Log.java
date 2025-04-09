package utils.log;

public class Log {
    private static Level level = Level.INFO;

    public static void setLevel(String level) {
        Log.level = Level.fromString(level);
    }

    public static void error(String message) {
        if (Level.ERROR.ordinal() <= level.ordinal()) {
            System.out.println(Colors.RED_BOLD + "[ERROR]: " + message + Colors.RESET);
        }
    }

    public static void warn(String message) {
        if (Level.WARN.ordinal() <= level.ordinal()) {
            System.out.println(Colors.YELLOW + "[WARN]: " + message + Colors.RESET);
        }
    }

    public static void info(String message) {
        if (Level.INFO.ordinal() <= level.ordinal()) {
            System.out.println(Colors.GREEN + "[INFO]: " + message + Colors.RESET);
        }
    }

    public static void debug(String message) {
        if (Level.DEBUG.ordinal() <= level.ordinal()) {
            System.out.println(Colors.CYAN + "[DEBUG]: " + message + Colors.RESET);
        }
    }

}

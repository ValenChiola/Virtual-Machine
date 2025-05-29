package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgsParser {
    private static ArgsParser instance;

    private String vmxFile;
    private String vmiFile;
    private int memory = 16384;
    private List<String> programParams = new ArrayList<>();
    private Map<String, String> flags = new HashMap<>();

    private ArgsParser(String[] args) {
        parse(args);
    }

    public static void build(String[] args) {
        instance = new ArgsParser(args);
    }

    public static ArgsParser getInstance() {
        if (instance == null)
            throw new IllegalStateException("ArgsParser no ha sido inicializado.");

        return instance;
    }

    private void parse(String[] args) {
        boolean pFlagFound = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (pFlagFound) {
                programParams.add(arg);
                continue;
            }

            if (arg.equals("-p")) {
                pFlagFound = true;
                continue;
            }

            if (arg.endsWith(".vmx")) {
                if (vmxFile != null)
                    System.err.println("Se ha proporcionado más de un archivo .vmx. Usando el primero.");
                else
                    vmxFile = arg;
            } else if (arg.endsWith(".vmi")) {
                if (vmiFile != null)
                    System.err.println("Se ha proporcionado más de un archivo .vmi. Usando el primero.");
                else
                    vmiFile = arg;
            } else if (arg.startsWith("m=")) {
                try {
                    memory = Integer.parseInt(arg.substring(2));
                } catch (NumberFormatException e) {
                    System.err.println("Formato de memoria inválido. Usando valor por defecto.");
                }
            } else if (arg.startsWith("-")) {
                String[] flagParts = arg.substring(1).split("=", 2);
                String flagName = flagParts[0];
                String flagValue = flagParts.length > 1 ? flagParts[1] : "true";
                flags.put(flagName, flagValue);
            }
        }

        if (!isValid())
            throw new IllegalArgumentException(
                    "No se han proporcionado argumentos válidos. Uso: java -jar vm.jar [archivo.vmx] [archivo.vmi] [-d] [m=<memoria>] [-log=<debug,warn,info,error>] [-p <param1> <param2> ...]");
    }

    private boolean isValid() {
        return vmxFile != null || vmiFile != null;
    }

    public static String getVmxFile() {
        return getInstance().vmxFile;
    }

    public static String getVmiFile() {
        return getInstance().vmiFile;
    }

    public static boolean isDisassemblerEnabled() {
        return getFlag("d") != null;
    }

    public static String getLogLevel() {
        String level = getFlag("log");

        return level != null ? level : "info";
    }

    public static int getMemory() {
        return getInstance().memory;
    }

    public static List<String> getProgramParams() {
        return getInstance().programParams;
    }

    public static String getFlag(String flagName) {
        return getInstance().flags.get(flagName);
    }

    public static boolean hasFlag(String flagName) {
        return getInstance().flags.containsKey(flagName);
    }

    @Override
    public String toString() {
        return "ArgsParser [vmxFile=" + vmxFile + ", vmiFile=" + vmiFile + ", memory=" + memory + ", programParams="
                + programParams + ", flags=" + flags + "]";
    }

}

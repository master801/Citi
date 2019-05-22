package org.slave.citi;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

@UtilityClass
public final class Citi {

    public static final Boolean DEBUG = Boolean.valueOf(System.getProperty("org.slave.citi.debug", Boolean.TRUE.toString()).toLowerCase());

    private static File fileDirectoryStart;

    private static File fileDirectoryCiti;

    private static File fileDirectoryCitiLibs;
    private static File fileDirectoryCitiConfig;
    private static File fileDirectoryCitiMods;

    public static final Logger LOGGER_CITI = Logger.getLogger("Citi");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        if (!Citi.getDirectoryCiti().exists()) Citi.getDirectoryCiti().mkdirs();
        if (!Citi.getDirectoryCitiLibs().exists() && !DEBUG) {
            LOGGER_CITI.log(Level.SEVERE, "Folder \'libs\' was not found?!");
            return;
        }
        if (!Citi.getDirectoryCitiConfig().exists()) Citi.getDirectoryCitiConfig().mkdir();
        if (!Citi.getDirectoryCitiMods().exists()) Citi.getDirectoryCitiMods().mkdir();
    }

    public static File getDirectoryStart() {
        if (Citi.fileDirectoryStart == null) {
            try {
                Field field = xaos.utils.f.class.getDeclaredField("a");//startFile
                field.setAccessible(true);
                Citi.fileDirectoryStart = (File)field.get(null);
            } catch(NoSuchFieldException | IllegalAccessException e) {
                LOGGER_CITI.log(Level.SEVERE, "Failed to get start directory due to an exception!", e);
            }
        }
        return Citi.fileDirectoryStart;
    }

    public static File getDirectoryCiti() {
        if (Citi.fileDirectoryCiti == null) {
            Citi.fileDirectoryCiti = new File(Citi.getDirectoryStart(), "citi");
        }
        return Citi.fileDirectoryCiti;
    }

    public static File getDirectoryCitiLibs() {
        if (Citi.fileDirectoryCitiLibs == null) {
            Citi.fileDirectoryCitiLibs = new File(Citi.getDirectoryCiti(), "libs");
        }
        return Citi.fileDirectoryCitiLibs;
    }

    public static File getDirectoryCitiConfig() {
        if (Citi.fileDirectoryCitiConfig == null) {
            Citi.fileDirectoryCitiConfig = new File(Citi.getDirectoryCiti(), "config");
        }
        return Citi.fileDirectoryCitiConfig;
    }

    public static File getDirectoryCitiMods() {
        if (Citi.fileDirectoryCitiMods == null) {
            Citi.fileDirectoryCitiMods = new File(Citi.getDirectoryCiti(), "mods");
        }
        return Citi.fileDirectoryCitiMods;
    }

}

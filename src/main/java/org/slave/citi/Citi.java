package org.slave.citi;

import lombok.experimental.UtilityClass;
import org.slave.citi.loader.CitiLoader;

import java.io.File;
import java.util.logging.Logger;

@UtilityClass
public final class Citi {

    public static final Boolean DEBUG = Boolean.valueOf(System.getProperty("org.slave.citi.debug", Boolean.TRUE.toString()).toLowerCase());

    public static final File DIRECTORY_CITI = new File("citi");

    public static final File DIRECTORY_CITI_LIBS = new File(DIRECTORY_CITI, "libs");
    public static final File DIRECTORY_CITI_CONFIG = new File(DIRECTORY_CITI, "configs");
    public static final File DIRECTORY_CITI_MODS = new File(DIRECTORY_CITI, "mods");

    public static final Logger LOGGER_CITI = Logger.getLogger("Citi");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        if (!Citi.DIRECTORY_CITI.exists()) Citi.DIRECTORY_CITI.mkdirs();
        if (!Citi.DIRECTORY_CITI_CONFIG.exists()) Citi.DIRECTORY_CITI_CONFIG.mkdirs();
        if (!Citi.DIRECTORY_CITI_MODS.exists()) Citi.DIRECTORY_CITI_MODS.mkdirs();

        CitiLoader.INSTANCE.loadMods();
    }

}

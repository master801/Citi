package org.slave.citi;

import java.io.File;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class Citi {

    public static final File DIRECTORY_CITI = new File("citi");

    public static final File DIRECTORY_CITI_CONFIG = new File(DIRECTORY_CITI, "configs");
    public static final File DIRECTORY_CITI_MODS = new File(DIRECTORY_CITI, "mods");

    public static final Logger LOGGER_CITI = LogManager.getLogManager().getLogger("Citi");

}

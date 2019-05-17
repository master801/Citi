package org.slave.citi.test;

import org.slave.citi.api.Mod;
import org.slave.citi.api.StageHandler;
import org.slave.citi.api.stage.StageInitialization;
import org.slave.citi.api.stage.StagePostInitialization;
import org.slave.citi.api.stage.StagePreInitialization;

import java.util.logging.Logger;

@Mod(name = "Example", version = "1.0.0")
public final class ExampleMod {

    public static final Logger LOGGER = Logger.getLogger("Example");

    @StageHandler
    public void stagePreInitialization(final StagePreInitialization stagePreInitialization) {
        LOGGER.info("We hit preInitialization!");
    }

    @StageHandler
    public void stageInitialization(final StageInitialization stageInitialization) {
        LOGGER.info("We hit initialization!");
    }

    @StageHandler
    public void stagePostInitialization(final StagePostInitialization stagePostInitialization) {
        LOGGER.info("We hit postInitialization!");
    }

}

package org.slave.citi.mod.test;

import org.slave.citi.api.EventHandler;
import org.slave.citi.api.Mod;
import org.slave.citi.api.event.stage.EventInitialization;
import org.slave.citi.api.event.stage.EventPostInitialization;
import org.slave.citi.api.event.stage.EventPreInitialization;

import java.util.logging.Logger;

/**
 * Created by Master on 5/22/19 at 10:56 AM
 *
 * @author Master
 */
@Mod(name = "Test", version = "1.0.0")
public final class TestMod {

    public static final Logger LOGGER = Logger.getLogger("Test-Mod");

    @EventHandler
    public void stagePreInitialization(final EventPreInitialization stagePreInitialization) {
        LOGGER.info("We hit preInitialization!");
    }

    @EventHandler
    public void stageInitialization(final EventInitialization stageInitialization) {
        LOGGER.info("We hit initialization!");
    }

    @EventHandler
    public void stagePostInitialization(final EventPostInitialization stagePostInitialization) {
        LOGGER.info("We hit postInitialization!");
    }

}

package org.slave.citi.api.event.stage;

import xaos.main.a;

/**
 * <p>
 *     Called before the display is initialized and also before textures are found
 * </p>
 *
 * Created by Master on 5/17/19 at 7:56 AM
 *
 * @author Master
 */
public final class EventInitialization extends EventStage {

    public EventInitialization(final a instance) {
        super(Stage.INITIALIZATION.name(), Stage.INITIALIZATION, instance);
    }

}

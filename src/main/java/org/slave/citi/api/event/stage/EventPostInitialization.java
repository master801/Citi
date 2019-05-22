package org.slave.citi.api.event.stage;

import xaos.main.a;

/**
 * <p>
 *     Called right before the game starts looping
 * </p>
 *
 * Created by Master on 5/17/19 at 7:57 AM
 *
 * @author Master
 */
public final class EventPostInitialization extends EventStage {

    public EventPostInitialization(final a instance) {
        super(Stage.POSTINITIALIZATION.name(), Stage.POSTINITIALIZATION, instance);
    }

}

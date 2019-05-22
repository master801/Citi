package org.slave.citi.api.event.stage;

import xaos.main.a;

/**
 * <p>
 *     Called after settings are initialized
 * </p>
 *
 * Created by Master on 5/17/19 at 7:53 AM
 *
 * @author Master
 */
public final class EventPreInitialization extends EventStage {

    public EventPreInitialization(final a instance) {
        super(Stage.PREINITIALIZATION.name(), Stage.PREINITIALIZATION, instance);
    }

}

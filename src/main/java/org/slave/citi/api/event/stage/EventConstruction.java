package org.slave.citi.api.event.stage;

import xaos.main.a;

/**
 * <p>
 *     Called when the game is constructing.<br/>
 *     <b>BE CAREFUL!! THIS IS CALLED BEFORE ANY GAME SETTINGS ARE SET!!</b>
 * </p>
 *
 * Created by Master on 5/20/19 at 8:42 AM
 *
 * @author Master
 */
public final class EventConstruction extends EventStage {

    public EventConstruction(final a instance) {
        super(Stage.CONSTRUCTION.name(), Stage.CONSTRUCTION, instance);
    }

}

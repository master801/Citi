package org.slave.citi.api.event.stage;

import lombok.Getter;
import org.slave.citi.api.event.Event;
import xaos.main.a;

/**
 * Created by Master on 5/17/19 at 7:52 AM
 *
 * @author Master
 */
public abstract class EventStage extends Event {

    @Getter
    private final Stage stage;

    @Getter
    private final a instance;

    public EventStage(final String name, final Stage stage, final a instance) {
        super(name);
        this.stage = stage;
        this.instance = instance;
    }

}

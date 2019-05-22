package org.slave.citi.api.event.stage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Master on 5/19/19 at 9:34 PM
 *
 * @author Master
 */
@RequiredArgsConstructor
public enum Stage {

    CONSTRUCTION(EventConstruction.class),

    PREINITIALIZATION(EventPreInitialization.class),

    INITIALIZATION(EventInitialization.class),

    POSTINITIALIZATION(EventPostInitialization.class);

    @Getter
    private final Class<? extends EventStage> eventClass;

    public static Stage from(final Class<?> clazz) {
        if (clazz == null || !EventStage.class.isAssignableFrom(clazz)) return null;
        for(Stage stage : Stage.values()) {
            if (stage.getEventClass() == clazz) return stage;
        }
        return null;
    }

}

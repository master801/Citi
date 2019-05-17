package org.slave.citi.api.stage;

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
public final class StagePostInitialization extends Stage {

    public StagePostInitialization(final a instance) {
        super("postInitialization", instance);
    }

}

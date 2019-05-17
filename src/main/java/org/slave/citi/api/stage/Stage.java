package org.slave.citi.api.stage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xaos.main.a;

/**
 * Created by Master on 5/17/19 at 7:52 AM
 *
 * @author Master
 */
@RequiredArgsConstructor
public abstract class Stage {

    @Getter
    private final String name;

    @Getter
    private final a instance;

}

package org.slave.citi.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Master on 5/22/19 at 11:53 AM
 *
 * @author Master
 */
@RequiredArgsConstructor
public abstract class Event {

    @Getter
    private final String name;

}

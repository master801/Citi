package org.slave.citi.loader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * Created by Master on 5/16/19 at 3:48 PM
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class CitiModCandidate {

    @Getter
    private final File file;

    public void explore() {
    }

}

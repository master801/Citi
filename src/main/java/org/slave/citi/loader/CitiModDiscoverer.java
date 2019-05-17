package org.slave.citi.loader;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CitiModDiscoverer {

    @Getter
    private final List<CitiModCandidate> modCandidates = Lists.newArrayList();

    public void findMods(final File dir) {
        List<File> list = new ArrayList<>();

        Collection<File> foundFiles = FileUtils.listFiles(
                dir,
                new String[]{
                        ".jar",
                        ".zip"
                },
                true
        );
        for (File foundFile : foundFiles) {
            modCandidates.add(
                    new CitiModCandidate(foundFile)
            );
        }
    }

}

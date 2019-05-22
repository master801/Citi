package org.slave.citi.loader;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public final class CitiModDiscoverer {

    private static final Pattern PATTERN_JAR = Pattern.compile("(.+).jar$");

    @Getter
    private final List<CitiModCandidate> modCandidates = Lists.newArrayList();

    public void findMods(final File dir) {
        Collection<File> foundFiles = FileUtils.listFiles(
                dir,
                new IOFileFilter() {

                    @Override
                    public boolean accept(final File file) {
                        return accept(file.getParentFile(), file.getName());
                    }

                    @Override
                    public boolean accept(final File dir, final String name) {
                        return PATTERN_JAR.matcher(name).matches();
                    }

                },
                TrueFileFilter.TRUE
        );
        for (File foundFile : foundFiles) {
            modCandidates.add(
                    new CitiModCandidate(foundFile)
            );
        }
    }

}

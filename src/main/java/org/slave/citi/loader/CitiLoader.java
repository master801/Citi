package org.slave.citi.loader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.citi.Citi;
import org.slave.citi.api.stage.Stage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CitiLoader {

    public static final CitiLoader INSTANCE = new CitiLoader();

    private final CitiModDiscoverer modDiscoverer = new CitiModDiscoverer();

    public void fireStage(final Stage stage) {
        Citi.LOGGER_CITI.info(String.format("Stage Fired! %s", stage.getName()));
    }

    public void loadMods() {
        findMods();
    }

    private void findMods() {
        modDiscoverer.findMods(Citi.DIRECTORY_CITI_MODS);

        if (modDiscoverer.getModCandidates().size() > 0) {
            Citi.LOGGER_CITI.info(String.format("Found %d mods!", modDiscoverer.getModCandidates().size()));
        } else {
            Citi.LOGGER_CITI.info("Found no mods");
        }
    }

}

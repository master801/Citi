package org.slave.citi.loader;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import org.slave.citi.Agent;
import org.slave.citi.Citi;
import org.slave.citi.api.LoadType;
import org.slave.citi.api.Mod;
import org.slave.citi.api.asm.ASM;
import org.slave.citi.api.event.stage.EventStage;
import org.slave.citi.api.event.stage.EventConstruction;
import org.slave.citi.loader.asm.CitiASMLoader;
import org.slave.lib.resources.ASMTable.TableClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class CitiLoader {

    public static final CitiLoader INSTANCE = new CitiLoader();

    private final CitiModDiscoverer modDiscoverer = new CitiModDiscoverer();
    private final List<CitiModContainer> modContainerList = new ArrayList<>();

    @Getter
    private final EventBus eventBus = new EventBus("Citi-Loader");

    //Event bus for mod containers
    private final EventBus eventBusMods = new EventBus("Citi-Loader-Mods");

    private CitiLoader() {
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(final EventStage event) {
        Citi.LOGGER_CITI.info(String.format("Firing event %s", event.getName()));

        if (event instanceof EventConstruction) {
            loadMods();
            return;
        }

        for(CitiModContainer modContainer : modContainerList) {
            Citi.LOGGER_CITI.log(Level.FINE, "Sending event %s to mod %s during stage %s", new Object[] { event.getName(), modContainer.getName(), event.getStage().name() });
            eventBusMods.post(event);
        }
    }

    public void loadMods() {
        findMods();
        addMods();
        injectModData();
    }

    private void findMods() {
        modDiscoverer.findMods(Citi.getDirectoryCitiMods());

        if (modDiscoverer.getModCandidates().size() > 0) {
            Citi.LOGGER_CITI.info(String.format("Found %d candidate mods!", modDiscoverer.getModCandidates().size()));
        } else {
            Citi.LOGGER_CITI.info("Found no mods");
        }

        for(CitiModCandidate citiModCandidate : modDiscoverer.getModCandidates()) {
            try {
                citiModCandidate.explore();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMods() {
        for(CitiModCandidate modCandidate : modDiscoverer.getModCandidates()) {
            if (!modCandidate.getModTableList().isEmpty() || !modCandidate.getAsmTableList().isEmpty()) {//If has asm or mod
                CitiClassLoader.getCitiClassLoader().addFile(modCandidate.getFile());
                CitiModContainer citiModContainer = new CitiModContainer(modCandidate);
                eventBusMods.register(citiModContainer);
                modContainerList.add(citiModContainer);
            }
        }
    }

    private void injectModData() {
        for(CitiModContainer modContainer : modContainerList) {
            if (!modContainer.getModCandidate().getAsmTableList().isEmpty()) {
                for(TableClass tableClass : modContainer.getModCandidate().getAsmTableList()) {
                    Class<?> asmClass = null;
                    try {
                        asmClass = CitiClassLoader.getCitiClassLoader().loadClass(tableClass.getName());
                    } catch (ClassNotFoundException e) {
                        Agent.LOGGER_CITI_AGENT.log(Level.SEVERE, "Caught exception while loading ASM class! Exception: %s", e.toString());
                    }

                    if (asmClass != null) {
                        ASM asm = asmClass.getAnnotation(ASM.class);
                        CitiASMLoader.INSTANCE.getTransformerClassList().addAll(
                                Lists.newArrayList(asm.transformers())
                        );

                        modContainer.setLoadType(LoadType.ASM);
                        modContainer.setMainClass(asmClass);
                    }
                }
            }
            if (!modContainer.getModCandidate().getModTableList().isEmpty()) {
                for(TableClass tableClass : modContainer.getModCandidate().getModTableList()) {
                    Class<?> modClass = null;
                    try {
                        modClass = CitiClassLoader.getCitiClassLoader().loadClass(tableClass.getName().replace('/', '.'));
                    } catch (ClassNotFoundException e) {
                        Agent.LOGGER_CITI_AGENT.log(Level.SEVERE, "Caught exception while loading Mod class! Exception: %s", e.toString());
                    }

                    if (modClass != null) {
                        Mod mod = modClass.getAnnotation(Mod.class);
                        modContainer.setName(mod.name());
                        modContainer.setName(mod.version());

                        modContainer.setLoadType(LoadType.MOD);
                        modContainer.setMainClass(modClass);
                        modContainer.injectData();
                    }
                }
            }
        }
    }

}

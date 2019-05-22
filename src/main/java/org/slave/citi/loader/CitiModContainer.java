package org.slave.citi.loader;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slave.citi.api.LoadType;
import org.slave.citi.api.event.stage.EventStage;
import org.slave.citi.api.event.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Master on 5/19/19 at 9:18 PM
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class CitiModContainer {

    @Getter(AccessLevel.PROTECTED)
    private final CitiModCandidate modCandidate;

    @Setter(AccessLevel.PROTECTED)
    @Getter
    private LoadType loadType;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private Class<?> mainClass;

    private Object instance;

    private final Map<Stage, Method> eventMethods = new HashMap<>();

    @Setter(AccessLevel.PROTECTED)
    @Getter
    private String name;

    @Setter(AccessLevel.PROTECTED)
    @Getter
    private String version;

    void injectData() {
        if (mainClass == null) {
            return;
        }

        try {
            Constructor<?> constructor = mainClass.getDeclaredConstructor();
            instance = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (instance == null) {
            return;
        }

        for(Method method : mainClass.getDeclaredMethods()) {
            if (EventStage.class.isAssignableFrom(method.getParameterTypes()[0])) {
                Stage stage = Stage.from(method.getParameterTypes()[0]);
                if (stage == null) {
                    continue;
                }
                eventMethods.put(stage, method);
            }
        }
    }

    @Subscribe
    public void fireEvent(final EventStage event) {
        if (event == null || loadType != LoadType.MOD) return;
        Method eventMethod = eventMethods.get(event.getStage());
        if (eventMethod == null) {
            return;
        } else {
            try {
                eventMethod.invoke(instance, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}

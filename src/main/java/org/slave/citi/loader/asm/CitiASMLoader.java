package org.slave.citi.loader.asm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slave.citi.api.asm.ASM;
import org.slave.lib.api.asm.Transformer;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Master on 5/19/19 at 9:57 PM
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CitiASMLoader {

    public static final CitiASMLoader INSTANCE = new CitiASMLoader();

    public static final Logger LOGGER_CITI_ASM = Logger.getLogger("Citi-ASM");

    @Getter
    private final List<Class<? extends Transformer>> transformerClassList = new ArrayList<>();

    public void sortTransformers() {
        transformerClassList.sort(
                Comparator.comparingInt(o -> o.getAnnotation(ASM.class).priority())
        );
    }

    public void loadTransformers(final Instrumentation instrumentation) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for(Class<? extends Transformer> transformerClass : transformerClassList) {
            Constructor<? extends Transformer> constructor = transformerClass.getConstructor();
            if (constructor == null) continue;
            Object instance = constructor.newInstance();
            Method methodTransform = Transformer.class.getMethod("transform", byte[].class, String.class, String.class);

            if (methodTransform == null) continue;

            instrumentation.addTransformer(
                    (loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
                        try {
                            return (byte[])methodTransform.invoke(instance, className, className, classfileBuffer);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                        }
                        return classfileBuffer;
                    }
            );
        }
    }

}

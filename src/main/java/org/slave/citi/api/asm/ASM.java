package org.slave.citi.api.asm;

import org.slave.lib.api.asm.Transformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Master on 5/17/19 at 9:51 AM
 *
 * @author Master
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ASM {

    Class<? extends Transformer>[] transformers();

    /**
     * @return Leave 0 for default loading. Lower priority (-1 or lower) loads first and a higher priority (100++) loads later (or last).
     */
    int priority() default 0;

}

package org.slave.citi.api.asm;

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

}

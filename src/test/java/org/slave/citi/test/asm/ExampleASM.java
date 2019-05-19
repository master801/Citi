package org.slave.citi.test.asm;

import org.slave.citi.api.asm.ASM;
import org.slave.citi.test.asm.transformers.TransformerExample;

/**
 * Created by Master on 5/17/19 at 9:52 AM
 *
 * @author Master
 */
@ASM(transformers = TransformerExample.class)
public class ExampleASM {
}

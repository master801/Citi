package org.slave.citi.test.asm.transformers;

import org.slave.citi.api.asm.Transformer;

/**
 * Created by Master on 5/17/19 at 9:53 AM
 *
 * @author Master
 */
public class TransformerExample implements Transformer {

    @Override
    public byte[] transform(final String className, final String transformedClassName, final byte[] original) {
        return original;
    }

}

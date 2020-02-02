package org.slave.citi.asm.transformers;

import lombok.NonNull;
import org.objectweb.asm.tree.ClassNode;
import org.slave.citi.Agent;
import org.slave.citi.Citi;
import org.slave.lib.asm.transformers.BasicTransformer;

/**
 * Created by Master on 5/22/19 at 5:41 PM
 *
 * @author Master
 */
public final class TransformerPanelMainMenu extends BasicTransformer {

    public TransformerPanelMainMenu() {
        super(Agent.LOGGER_CITI_AGENT);
    }

    @Override
    protected void transform(@NonNull final ClassNode classNode) {
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return isNameTransformed ? "xaos/panels/PanelMainMenu" : "xaos/panels/a";
    }

    @Override
    protected boolean writeClassFile() {
        return Citi.DEBUG;
    }

    @Override
    protected boolean writeASMFile() {
        return Citi.DEBUG;
    }

}

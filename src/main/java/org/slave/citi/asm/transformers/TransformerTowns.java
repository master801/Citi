package org.slave.citi.asm.transformers;

import lombok.NonNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.citi.Agent;
import org.slave.citi.Citi;
import org.slave.lib.asm.transformers.BasicTransformer;

/**
 * <p>
 *     Replaces Towns' current running classloader with ours
 * </p>
 *
 * Created by Master on 5/17/19 at 6:49 AM
 *
 * @author Master
 */
public final class TransformerTowns extends BasicTransformer {

    public TransformerTowns() {
        super(Agent.LOGGER_CITI_AGENT);
    }

    @Override
    protected void transform(@NonNull final ClassNode classNode) {
    }

    @Override
    protected ClassVisitor getClassVisitor(final ClassNode classNode) {
        return new ClassVisitor(Opcodes.ASM5, classNode) {

            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                //Replace classloader
                if (access == (Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC) && name.equals("main") && desc.equals("([Ljava/lang/String;)V")) {
                    return new MethodVisitor(super.api, mv) {

                        @Override
                        public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
                            super.visitTryCatchBlock(start, end, handler, type);

                            if (type.equals("java/lang/Exception")) {
                                super.visitLabel(new Label());
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/citi/loader/CitiClassLoader", "replaceCurrentClassLoader", "()V", false);
                                super.visitLabel(new Label());
                            }
                        }

                    };
                }
                return mv;
            }
        };
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "xaos/Towns";
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

package org.slave.citi.asm.transformers;

import lombok.NonNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.slave.citi.Agent;
import org.slave.citi.Citi;
import org.slave.lib.asm.transformers.BasicTransformer;

/**
 * Created by Master on 5/17/19 at 8:02 AM
 *
 * @author Master
 */
public final class TransformerTownsMain extends BasicTransformer {

    public TransformerTownsMain() {
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

                //Load mods and fire stages
                if (access == Opcodes.ACC_PUBLIC && name.equals("<init>") && desc.equals("()V")) {
                    return new MethodVisitor(super.api, mv) {

                        @Override
                        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                            super.visitFieldInsn(opcode, owner, name, desc);

                            if (opcode == Opcodes.PUTSTATIC && owner.equals(classNode.name)) {
                                if (name.equals("d") && desc.equals(Type.INT_TYPE.getDescriptor())) {
                                    //Fire preInitialization stage for mods
                                    super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                    super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "getEventBus", "()Lcom/google/common/eventbus/EventBus;", false);
                                    super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/event/stage/EventPreInitialization");
                                    super.visitInsn(Opcodes.DUP);
                                    super.visitVarInsn(Opcodes.ALOAD, 0);
                                    super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/event/stage/EventPreInitialization", "<init>", "(Lxaos/main/a;)V", false);
                                    super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/google/common/eventbus/EventBus", "post", "(Ljava/lang/Object;)V", false);
                                }
                            }
                        }

                        @Override
                        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                            super.visitMethodInsn(opcode, owner, name, desc, itf);

                            if (opcode == Opcodes.INVOKESTATIC && owner.equals("xaos/actions/a") && name.equals("a") && desc.equals("(II)V")) {
                                //Fire initialization stage
                                super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "getEventBus", "()Lcom/google/common/eventbus/EventBus;", false);
                                super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/event/stage/EventInitialization");
                                super.visitInsn(Opcodes.DUP);
                                super.visitVarInsn(Opcodes.ALOAD, 0);
                                super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/event/stage/EventInitialization", "<init>", "(Lxaos/main/a;)V", false);
                                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/google/common/eventbus/EventBus", "post", "(Ljava/lang/Object;)V", false);
                            } else if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("xaos/panels/c") && name.equals("b") && desc.equals("(Z)V")) {
                                //Fire postInitialization stage
                                super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "getEventBus", "()Lcom/google/common/eventbus/EventBus;", false);
                                super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/event/stage/EventPostInitialization");
                                super.visitInsn(Opcodes.DUP);
                                super.visitVarInsn(Opcodes.ALOAD, 0);
                                super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/event/stage/EventPostInitialization", "<init>", "(Lxaos/main/a;)V", false);
                                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/google/common/eventbus/EventBus", "post", "(Ljava/lang/Object;)V", false);
                            } else if (opcode == Opcodes.INVOKESPECIAL && owner.equals("java/lang/Object") && name.equals("<init>") && desc.equals("()V")) {
                                //Fire construction stage
                                super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "getEventBus", "()Lcom/google/common/eventbus/EventBus;", false);
                                super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/event/stage/EventConstruction");
                                super.visitInsn(Opcodes.DUP);
                                super.visitVarInsn(Opcodes.ALOAD, 0);
                                super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/event/stage/EventConstruction", "<init>", "(Lxaos/main/a;)V", false);
                                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/google/common/eventbus/EventBus", "post", "(Ljava/lang/Object;)V", false);
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
        return isNameTransformed ? "xaos/main/TownsMain" : "xaos/main/a";
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

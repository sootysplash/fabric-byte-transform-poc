package me.sootysplash.swap.clinitInvoker;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;

import java.util.List;

public record OurMixinTransformer(IMixinTransformer parent) implements IMixinTransformer {

    public static final String movedMethodName = "moved_clinit";

    @Override
    public void audit(MixinEnvironment environment) {
        parent.audit(environment);
    }

    @Override
    public List<String> reload(String mixinClass, ClassNode classNode) {
        return parent.reload(mixinClass, classNode);
    }

    @Override // is no-op
    public boolean computeFramesForClass(MixinEnvironment environment, String name, ClassNode classNode) {
        return parent.computeFramesForClass(environment, name, classNode);
    }

    @Override // basically all non mixin classes go through here
    public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
        // this can be extracted but I ain't doing all that
        if (name.equals("me.sootysplash.dm.DummyMod")) {
            System.out.println("tcp: " + name + " | " + transformedName + " | " + basicClass.length);
            ClassNode cn = new ClassNode();
            new ClassReader(basicClass).accept(cn, ClassReader.EXPAND_FRAMES);

            MethodNode clinit = null;
            for (MethodNode mn : cn.methods) {
                if ("<clinit>".equals(mn.name)) {
                    clinit = mn;
                }
            }

            if (clinit == null) {
                System.out.println("failed to find clinit for: " + name);
                return parent.transformClassBytes(name, transformedName, basicClass);
            }

            MethodNode movedClinit = new MethodNode(0x9, "moved_clinit", "()V", null, null);
            movedClinit.instructions.add(clinit.instructions);

            clinit.instructions.clear();
            clinit.instructions.add(new LabelNode());
            clinit.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, cn.name, movedMethodName, "()V"));
            clinit.instructions.add(new LabelNode());
            clinit.instructions.add(new InsnNode(Opcodes.RETURN));

            cn.methods.add(movedClinit);

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES); // maybe should be COMPUTE_MAXS? Can't remember
            cn.accept(cw);
            return cw.toByteArray();
        }
        return parent.transformClassBytes(name, transformedName, basicClass);
    }

    @Override
    public byte[] transformClass(MixinEnvironment environment, String name, byte[] classBytes) {
        return parent.transformClass(environment, name, classBytes);
    }

    @Override
    public boolean transformClass(MixinEnvironment environment, String name, ClassNode classNode) {
        return parent.transformClass(environment, name, classNode);
    }

    @Override
    public boolean couldTransformClass(MixinEnvironment environment, String name) {
        return parent.couldTransformClass(environment, name);
    }

    @Override
    public byte[] generateClass(MixinEnvironment environment, String name) {
        return parent.generateClass(environment, name);
    }

    @Override
    public boolean generateClass(MixinEnvironment environment, String name, ClassNode classNode) {
        return parent.generateClass(environment, name, classNode);
    }

    @Override
    public IExtensionRegistry getExtensions() {
        return parent.getExtensions();
    }
}

package me.sootysplash.swap.clinitInvoker;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.lang.reflect.Field;

public class OnPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {
            // this probably shouldn't be done in prod, but I know at least half a dozen 'projects' that have been doing something similar for years
            System.out.println("init pre launch");

            // get the delegate instance through our knot instance
            ClassLoader knotClassLoader = OnPreLaunch.class.getClassLoader();
            Class<?> knotClassLoaderClass = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader");
            Field delegate = knotClassLoaderClass.getDeclaredField("delegate");
            delegate.trySetAccessible();
            Object delegateInstance = delegate.get(knotClassLoader);
            System.out.println("got delegate");

            // get the transformer
            Class<?> kcd = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
            Field mt = kcd.getDeclaredField("mixinTransformer");
            mt.trySetAccessible();
            IMixinTransformer transformer = (IMixinTransformer) mt.get(delegateInstance);
            System.out.println("got transformer");

            // inject our transformer
            mt.set(delegateInstance, new OurMixinTransformer(transformer));
            System.out.println("injected transformer");
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
    }
}

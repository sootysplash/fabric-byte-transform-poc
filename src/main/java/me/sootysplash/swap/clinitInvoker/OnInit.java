package me.sootysplash.swap.clinitInvoker;

import net.fabricmc.api.ModInitializer;

// testing stuff
public class OnInit implements ModInitializer {
    @Override
    public void onInitialize() {
        callClinitThing();
        new Thread(() -> {
            while (true) {
                callClinitThing();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static void callClinitThing() {
        try {
            Class.forName("me.sootysplash.dm.DummyMod").getMethod(OurMixinTransformer.movedMethodName).invoke(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

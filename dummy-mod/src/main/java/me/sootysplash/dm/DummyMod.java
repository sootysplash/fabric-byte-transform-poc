package me.sootysplash.dm;

import net.fabricmc.api.ModInitializer;

public class DummyMod implements ModInitializer {

    // be careful about final fields and whatnot
    static {
        System.out.println("DUMMY CLASS: clinit");
        new Throwable("Here is my trace").printStackTrace(System.out);
    }

    @Override
    public void onInitialize() {
        System.out.println("DUMMY CLASS: on initialize");
    }
}

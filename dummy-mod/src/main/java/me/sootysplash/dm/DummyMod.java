package me.sootysplash.dm;

import net.fabricmc.api.ModInitializer;

public class DummyMod implements ModInitializer {

    static {
        System.out.println("clinit!");
    }

    @Override
    public void onInitialize() {
    }
}

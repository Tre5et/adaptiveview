package net.treset.adaptiveview.tools;

import net.minecraft.server.MinecraftServer;

public class MinecraftServerInstance {
    private static MinecraftServer INSTANCE;

    public static MinecraftServer getInstance() { return INSTANCE; }
    public static void setInstance(MinecraftServer instance) { INSTANCE = instance; }
}

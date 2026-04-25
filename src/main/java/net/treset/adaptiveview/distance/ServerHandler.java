package net.treset.adaptiveview.distance;

import net.minecraft.server.MinecraftServer;
import net.treset.adaptiveview.AdaptiveViewMod;
import net.treset.adaptiveview.config.Config;
import net.treset.adaptiveview.config.ServerState;
import net.treset.adaptiveview.unlocking.LockManager;

import java.util.*;

public class ServerHandler {
    private TickLengthBuffer tickLengths;

    public TickLengthBuffer getTickLengths() {
        return tickLengths;
    }

    private final Config config;
    private final LockManager lockManager;
    private final ViewDistanceHandler viewDistanceHandler;

    private int tickCounter = 0;

    private int nextUpdate;

    public ServerHandler(Config config, LockManager lockManager, ViewDistanceHandler viewDistanceHandler) {
        this.config = config;
        this.lockManager = lockManager;
        this.viewDistanceHandler = viewDistanceHandler;
        this.nextUpdate = config.getUpdateRate();
        this.tickLengths = new TickLengthBuffer(config.getUpdateRate());
    }


    public void onTick(MinecraftServer server) {
        if(AdaptiveViewMod.isClient() && !config.isAllowOnClient()) {
            return;
        }
        lockManager.onTick();

        tickCounter++;

        if(tickCounter == nextUpdate) {
            ServerState state = new ServerState(
                    ViewDistanceHandler.getViewDistance(),
                    ViewDistanceHandler.getSimDistance(),
                    tickLengths.averageMillis(),
                    getMemory(),
                    getPlayers()
            );

            tickCounter = 0;
            nextUpdate = viewDistanceHandler.updateViewDistance(state);
            tickLengths.reset();
        }
    }

    public static double getMemory() {
        long allocatedMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        return allocatedMemory/(double)maxMemory*100;
    }

    public static List<String> getPlayers() {
        MinecraftServer server = AdaptiveViewMod.getServer();
        return Arrays.stream(server.getPlayerNames()).toList();
    }
}

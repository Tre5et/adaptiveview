package net.treset.adaptiveview.distance;

import net.treset.adaptiveview.config.Config;
import net.treset.adaptiveview.tools.MinecraftServerInstance;

public class ViewDistanceHandler {

    public static void updateViewDistance(long averageTicks) {
        if(Config.getLocked() != 0) return;
        if(averageTicks / 1000000 > Config.getMaxMspt()) {
            if(averageTicks / 1000000 > Config.getMaxMsptAggressive()) {
                addViewDitance(-2);
            } else addViewDitance(-1);
        } else if(averageTicks / 1000000 < Config.getMinMspt()) {
            if(averageTicks / 1000000 < Config.getMinMsptAggressive()) {
                addViewDitance(2);
            } else addViewDitance(1);
        }
    }

    public static void addViewDitance(int chunks) {
        int vd = Math.max(Config.getMinViewDistance(), Math.min(Config.getMaxViewDistance(), getViewDistance() + chunks));
        if(vd == getViewDistance()) return;
        setViewDistance(vd);
    }

    public static void setViewDistance(int chunks) {
        MinecraftServerInstance.getInstance().getPlayerManager().setViewDistance(chunks);
    }

    public static int getViewDistance() {
        return MinecraftServerInstance.getInstance().getPlayerManager().getViewDistance();
    }

}

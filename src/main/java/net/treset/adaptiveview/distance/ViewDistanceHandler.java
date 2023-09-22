package net.treset.adaptiveview.distance;

import net.treset.adaptiveview.AdaptiveViewMod;
import net.treset.adaptiveview.tools.MinecraftServerInstance;

public class ViewDistanceHandler {

    public static void updateViewDistance(long averageTicks) {
        if(AdaptiveViewMod.getConfig().getLocked() != 0) return;
        if(averageTicks / 1000000 > AdaptiveViewMod.getConfig().getMaxMspt()) {
            if(averageTicks / 1000000 > AdaptiveViewMod.getConfig().getMaxMsptAggressive()) {
                addViewDitance(-2);
            } else addViewDitance(-1);
        } else if(averageTicks / 1000000 < AdaptiveViewMod.getConfig().getMinMspt()) {
            if(averageTicks / 1000000 < AdaptiveViewMod.getConfig().getMinMsptAggressive()) {
                addViewDitance(2);
            } else addViewDitance(1);
        }
    }

    public static void addViewDitance(int chunks) {
        int vd = Math.max(AdaptiveViewMod.getConfig().getMinViewDistance(), Math.min(AdaptiveViewMod.getConfig().getMaxViewDistance(), getViewDistance() + chunks));
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

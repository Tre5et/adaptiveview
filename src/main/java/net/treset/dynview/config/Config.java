package net.treset.dynview.config;

public class Config {
    private static boolean locked = false;
    private static int updateInterval = 7;
    private static int minMspt = 4;
    private static int maxMspt = 5;
    private static int minMsptAggressive = 52;
    private static int maxMsptAggressive = 52;
    private static int minViewDistance = 4;
    private static int maxViewDistance = 20;

    public static boolean isLocked() {
        return locked;
    }

    public static int getUpdateInterval() {
        return updateInterval;
    }

    public static int getMinMspt() {
        return minMspt;
    }

    public static int getMaxMspt() {
        return maxMspt;
    }

    public static int getMinMsptAggressive() {
        return minMsptAggressive;
    }

    public static int getMaxMsptAggressive() {
        return maxMsptAggressive;
    }

    public static int getMinViewDistance() {
        return minViewDistance;
    }

    public static int getMaxViewDistance() {
        return maxViewDistance;
    }
}

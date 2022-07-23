package net.treset.dynview.config;

public class Config {
    private static int locked = 0;
    private static int updateInterval = 7;
    private static int minMspt = 4;
    private static int maxMspt = 5;
    private static int minMsptAggressive = 52;
    private static int maxMsptAggressive = 52;
    private static int minViewDistance = 4;
    private static int maxViewDistance = 20;

    public static int getLocked() {
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

    public static void setLocked(int locked) {
        Config.locked = locked;
    }

    public static void setUpdateInterval(int updateInterval) {
        Config.updateInterval = updateInterval;
    }

    public static void setMinMspt(int minMspt) {
        Config.minMspt = minMspt;
    }

    public static void setMaxMspt(int maxMspt) {
        Config.maxMspt = maxMspt;
    }

    public static void setMinMsptAggressive(int minMsptAggressive) {
        Config.minMsptAggressive = minMsptAggressive;
    }

    public static void setMaxMsptAggressive(int maxMsptAggressive) {
        Config.maxMsptAggressive = maxMsptAggressive;
    }

    public static void setMinViewDistance(int minViewDistance) {
        Config.minViewDistance = minViewDistance;
    }

    public static void setMaxViewDistance(int maxViewDistance) {
        Config.maxViewDistance = maxViewDistance;
    }
}

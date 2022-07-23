package net.treset.dynview.config;

public class Config {
    private static int updateInterval = 7;
    private static int minMspt = 40;
    private static int maxMspt = 48;
    private static int minMsptAggressive = 52;
    private static int maxMsptAggressive = 52;

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
}

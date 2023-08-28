package net.treset.adaptiveview.config;

import com.google.gson.JsonObject;
import net.treset.adaptiveview.AdaptiveviewMod;
import net.treset.adaptiveview.tools.FileTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Config {
    private static int locked = 0;
    private static int updateInterval = 600;
    private static int minMspt = 40;
    private static int maxMspt = 50;
    private static int minMsptAggressive = 60;
    private static int maxMsptAggressive = 30;
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

    public static void save() {
        JsonObject json = new JsonObject();
        json.addProperty("updateInterval", getUpdateInterval());
        json.addProperty("minMspt", getMinMspt());
        json.addProperty("maxMspt", getMaxMspt());
        json.addProperty("minMsptAggressive", getMinMsptAggressive());
        json.addProperty("maxMsptAggressive", getMaxMsptAggressive());
        json.addProperty("minViewDistance", getMinViewDistance());
        json.addProperty("maxViewDistance", getMaxViewDistance());
        FileTools.writeJsonToFile(json, new File("./config/adaptiveview.json"));
    }

    public static void load() {
        boolean oldConfig = false;
        File configFile = new File("./config/adaptiveview.json");
        if(!configFile.exists()) {
            configFile = new File("./config/dynview.json");
            if(configFile.exists()) {
                oldConfig = true;
            } else {
                save();
                return;
            }
        }

        JsonObject json = FileTools.readJsonFile(configFile);
        if(json == null) {
            save();
            return;
        }
        updateInterval = json.getAsJsonPrimitive("updateInterval").getAsInt();
        minMspt = json.getAsJsonPrimitive("minMspt").getAsInt();
        maxMspt = json.getAsJsonPrimitive("maxMspt").getAsInt();
        minMsptAggressive = json.getAsJsonPrimitive("minMsptAggressive").getAsInt();
        maxMsptAggressive = json.getAsJsonPrimitive("maxMsptAggressive").getAsInt();
        minViewDistance = json.getAsJsonPrimitive("minViewDistance").getAsInt();
        maxViewDistance = json.getAsJsonPrimitive("maxViewDistance").getAsInt();

        if(oldConfig) {
            try {
                Files.delete(configFile.toPath());
            } catch (IOException e) {
                AdaptiveviewMod.LOGGER.error("Failed to delete old config file", e);
            }
            save();
        }
    }
}

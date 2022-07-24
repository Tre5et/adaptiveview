package net.treset.adaptiveview.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.treset.adaptiveview.config.Config;
import net.treset.adaptiveview.tools.TextTools;

public class ConfigCommands {
    public static int base(CommandContext<ServerCommandSource> ctx) {
        TextTools.replyFormatted(ctx, "?iCurrent Configuration:", false);
        TextTools.replyFormatted(ctx, String.format("?iUpdate interval: ?B%s ticks", Config.getUpdateInterval()), false);
        TextTools.replyFormatted(ctx, String.format("?iTarget MSPT: ?B%s-%s MSPT", Config.getMinMspt(), Config.getMaxMspt()), false);
        TextTools.replyFormatted(ctx, String.format("?iTarget MSPT aggressive: ?B%s-%s MSPT", Config.getMinMsptAggressive(), Config.getMaxMsptAggressive()), false);
        TextTools.replyFormatted(ctx, String.format("?iView Distance Range: ?B%s-%s chunks", Config.getMinViewDistance(), Config.getMaxViewDistance()), false);
        return 1;
    }

    public static int updateInterval(CommandContext<ServerCommandSource> ctx) {
        TextTools.replyFormatted(ctx, String.format("?i?IThe interval in which the view distance is adjusted. Currently ?B%s ticks", Config.getUpdateInterval()), false);
        return 1;
    }

    public static int updateIntervalInterval(CommandContext<ServerCommandSource> ctx) {
        int interval = IntegerArgumentType.getInteger(ctx, "interval");
        int oldInterval = Config.getUpdateInterval();
        Config.setUpdateInterval(interval);
        Config.save();
        TextTools.replyFormatted(ctx, String.format("?gChanged update interval from ?B%s?B to ?B%s ticks", oldInterval, interval), true);
        return 1;
    }

    public static int targetMspt(CommandContext<ServerCommandSource> ctx) {
        TextTools.replyFormatted(ctx, String.format("?i?IThe MSPT range the server aims to stay in. Currently ?B%s-%s MSPT", Config.getMinMspt(), Config.getMaxMspt()), false);
        return 1;
    }

    public static int targetMsptMinMsptMaxMspt(CommandContext<ServerCommandSource> ctx) {
        int min = IntegerArgumentType.getInteger(ctx, "minMspt");
        int max = IntegerArgumentType.getInteger(ctx, "maxMspt");
        int oldMin = Config.getMinMspt();
        int oldMax = Config.getMaxMspt();
        Config.setMinMspt(min);
        Config.setMaxMspt(max);
        Config.save();
        TextTools.replyFormatted(ctx, String.format("?gChanged target MSPT range from ?B%s-%s?B to ?B%s-%s MSPT", oldMin, oldMax, min, max), true);
        return 1;
    }

    public static int targetMsptAggressive(CommandContext<ServerCommandSource> ctx) {
        TextTools.replyFormatted(ctx, String.format("?i?IThe MSPT range in which the view distance will be adjusted at max one chunk per update. Outside this range the view distance will be adjsuted two chunks at once. Currently ?B%s-%s MSPT", Config.getMinMsptAggressive(), Config.getMaxMsptAggressive()), false);
        return 1;
    }

    public static int targetMsptAggressiveMinMsptMaxMsptAggressive(CommandContext<ServerCommandSource> ctx) {
        int min = IntegerArgumentType.getInteger(ctx, "minMsptAggressive");
        int max = IntegerArgumentType.getInteger(ctx, "maxMsptAggressive");
        int oldMin = Config.getMinMsptAggressive();
        int oldMax = Config.getMaxMsptAggressive();
        Config.setMinMsptAggressive(min);
        Config.setMaxMsptAggressive(max);
        Config.save();
        TextTools.replyFormatted(ctx, String.format("?gChanged aggressive MSPT range from ?B%s-%s?B to ?B%s-%s MSPT", oldMin, oldMax, min, max), true);
        return 1;
    }

    public static int viewDistanceRange(CommandContext<ServerCommandSource> ctx) {
        TextTools.replyFormatted(ctx, String.format("?i?IThe view distance range the server will adjust view distance in. Currently ?B%s-%s chunks", Config.getMinViewDistance(), Config.getMaxViewDistance()), false);
        return 1;
    }

    public static int viewDistanceRangeMinVDMaxVD(CommandContext<ServerCommandSource> ctx) {
        int min = IntegerArgumentType.getInteger(ctx, "minVD");
        int max = IntegerArgumentType.getInteger(ctx, "maxVD");
        int oldMin = Config.getMinViewDistance();
        int oldMax = Config.getMaxViewDistance();
        Config.setMinViewDistance(min);
        Config.setMaxViewDistance(max);
        Config.save();
        TextTools.replyFormatted(ctx, String.format("?gChanged view distance range from ?B%s-%s?B to ?B%s-%s chunks", oldMin, oldMax, min, max), true);
        return 1;
    }
}

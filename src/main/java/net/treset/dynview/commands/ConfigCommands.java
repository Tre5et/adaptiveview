package net.treset.dynview.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.treset.dynview.config.Config;

public class ConfigCommands {
    public static int base(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal("Current Configuration:"), true);
        ctx.getSource().sendFeedback(Text.literal("Update interval: " + Config.getUpdateInterval()), true);
        ctx.getSource().sendFeedback(Text.literal(String.format("Target MSPT: %s to %s MSPT", Config.getMinMspt(), Config.getMaxMspt())), true);
        ctx.getSource().sendFeedback(Text.literal(String.format("Target MSPT aggressive: %s to %s MSPT", Config.getMinMsptAggressive(), Config.getMaxMsptAggressive())), true);
        ctx.getSource().sendFeedback(Text.literal(String.format("View Distance Range: %s to %s chunks", Config.getMinViewDistance(), Config.getMaxViewDistance())), true);
        return 1;
    }

    public static int updateInterval(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal(String.format("The interval in which the view distance is adjusted. Currently %s ticks", Config.getUpdateInterval())), true);
        return 1;
    }

    public static int updateIntervalInterval(CommandContext<ServerCommandSource> ctx) {
        int interval = IntegerArgumentType.getInteger(ctx, "interval");
        int oldInterval = Config.getUpdateInterval();
        Config.setUpdateInterval(interval);
        ctx.getSource().sendFeedback(Text.literal(String.format("Changed update interval from %s to %s ticks", oldInterval, interval)), true);
        return 1;
    }

    public static int targetMspt(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal(String.format("The MSPT range the server aims to stay in. Currently %s - %s MSPT", Config.getMinMspt(), Config.getMaxMspt())), true);
        return 1;
    }

    public static int targetMsptMinMsptMaxMspt(CommandContext<ServerCommandSource> ctx) {
        int min = IntegerArgumentType.getInteger(ctx, "minMspt");
        int max = IntegerArgumentType.getInteger(ctx, "maxMspt");
        int oldMin = Config.getMinMspt();
        int oldMax = Config.getMaxMspt();
        Config.setMinMspt(min);
        Config.setMaxMspt(max);
        ctx.getSource().sendFeedback(Text.literal(String.format("Changed target MSPT range from %s - %s to %s - %s MSPT", oldMin, oldMax, min, max)), true);
        return 1;
    }

    public static int targetMsptAggressive(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal(String.format("The MSPT range in which the view distance will be adjusted at max one chunk per update. Outside this range the view distance will be adjsuted two chunks at once. Currently %s - %s MSPT", Config.getMinMsptAggressive(), Config.getMaxMsptAggressive())), true);
        return 1;
    }

    public static int targetMsptAggressiveMinMsptMaxMsptAggressive(CommandContext<ServerCommandSource> ctx) {
        int min = IntegerArgumentType.getInteger(ctx, "minMsptAggressive");
        int max = IntegerArgumentType.getInteger(ctx, "maxMsptAggressive");
        int oldMin = Config.getMinMsptAggressive();
        int oldMax = Config.getMaxMsptAggressive();
        Config.setMinMsptAggressive(min);
        Config.setMaxMsptAggressive(max);
        ctx.getSource().sendFeedback(Text.literal(String.format("Changed aggressive MSPT range from %s - %s to %s - %s MSPT", oldMin, oldMax, min, max)), true);
        return 1;
    }

    public static int viewDistanceRange(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal(String.format("The view distance range the server will adjust view distance in. Currently %s - %s chunks", Config.getMinViewDistance(), Config.getMaxViewDistance())), true);
        return 1;
    }

    public static int viewDistanceRangeMinVDMaxVD(CommandContext<ServerCommandSource> ctx) {
        int min = IntegerArgumentType.getInteger(ctx, "minVD");
        int max = IntegerArgumentType.getInteger(ctx, "maxVD");
        int oldMin = Config.getMinViewDistance();
        int oldMax = Config.getMaxViewDistance();
        Config.setMinViewDistance(min);
        Config.setMaxViewDistance(max);
        ctx.getSource().sendFeedback(Text.literal(String.format("Changed view distance range from %s - %s to %s - %s chunks", oldMin, oldMax, min, max)), true);
        return 1;
    }
}

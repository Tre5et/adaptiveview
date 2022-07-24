package net.treset.adaptiveview.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.treset.adaptiveview.distance.ViewDistanceHandler;
import net.treset.adaptiveview.tools.TextTools;

public class CommandHandler {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandManager.RegistrationEnvironment environment) {
        if(!environment.dedicated) return;
        dispatcher.register(CommandManager.literal("adaptiveview")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(CommandHandler::dynview)
                .then(CommandManager.literal("config")
                        .executes(ConfigCommands::base)
                        .then(CommandManager.literal("updateInterval")
                                .executes(ConfigCommands::updateInterval)
                                .then(CommandManager.argument("interval", IntegerArgumentType.integer(1, 72000))
                                        .executes(ConfigCommands::updateIntervalInterval)
                                )
                        )
                        .then(CommandManager.literal("targetMspt")
                                .executes(ConfigCommands::targetMspt)
                                .then(CommandManager.argument("minMspt", IntegerArgumentType.integer(1, 499))
                                        .then(CommandManager.argument("maxMspt", IntegerArgumentType.integer(2, 500))
                                                .executes(ConfigCommands::targetMsptMinMsptMaxMspt)
                                        )
                                )
                        )
                        .then(CommandManager.literal("targetMsptAggressive")
                                .executes(ConfigCommands::targetMsptAggressive)
                                .then(CommandManager.argument("minMsptAggressive", IntegerArgumentType.integer(1, 499))
                                        .then(CommandManager.argument("maxMsptAggressive", IntegerArgumentType.integer(2, 500))
                                                .executes(ConfigCommands::targetMsptAggressiveMinMsptMaxMsptAggressive)
                                        )
                                )
                        )
                        .then(CommandManager.literal("viewDistanceRange")
                                .executes(ConfigCommands::viewDistanceRange)
                                .then(CommandManager.argument("minVD", IntegerArgumentType.integer(3, 31))
                                        .then(CommandManager.argument("maxVD", IntegerArgumentType.integer(4, 32))
                                                .executes(ConfigCommands::viewDistanceRangeMinVDMaxVD)
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("lock")
                        .executes(LockCommands::base)
                        .then(CommandManager.literal("set")
                                .executes(LockCommands::set)
                                .then(CommandManager.argument("chunks", IntegerArgumentType.integer(3, 32))
                                        .executes(LockCommands::setChunks)
                                        .then(CommandManager.literal("timeout")
                                                .executes(LockCommands::setChunksTimeout)
                                                .then(CommandManager.argument("ticks", IntegerArgumentType.integer(1))
                                                        .executes(LockCommands::setChunksTimeoutTicks)
                                                )
                                        )
                                        .then(CommandManager.literal("player")
                                                .executes(LockCommands::setChunksPlayer)
                                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                                        .then(CommandManager.literal("disconnect")
                                                                .executes(LockCommands::setChunksPlayerDisconnect)
                                                        )
                                                        .then(CommandManager.literal("move")
                                                                .executes(LockCommands::setChunksPlayerMove)
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("unlock")
                                .executes(LockCommands::unlock)
                                .then(CommandManager.literal("clear")
                                        .executes(LockCommands::clear)
                                )
                        )
                )
        );
    }

    private static int dummyExec(CommandContext<ServerCommandSource> ctx) {
        return 1;
    }

    private static int dynview(CommandContext<ServerCommandSource> ctx) {
        TextTools.replyFormatted(ctx, String.format("?iThe current view distance is ?B%s chunks", ViewDistanceHandler.getViewDistance()), true);
        return 1;
    }
}

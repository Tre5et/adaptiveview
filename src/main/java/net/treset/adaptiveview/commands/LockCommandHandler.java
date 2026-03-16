package net.treset.adaptiveview.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.treset.adaptiveview.AdaptiveViewMod;
import net.treset.adaptiveview.config.Config;
import net.treset.adaptiveview.tools.TextTools;
import net.treset.adaptiveview.unlocking.*;

public class LockCommandHandler {
    private final Config config;
    private final LockManager lockManager;

    public LockCommandHandler(Config config, LockManager lockManager) {
        this.config = config;
        this.lockManager = lockManager;
    }

    private void replyAndBroadcastLock(CommandContext<CommandSourceStack> ctx, String message, Object... args) {
        TextTools.replyAndBroadcastIf((p) -> LockManager.shouldBroadcastLock(p, config), ctx, message, args);
    }

    private int lockStatus(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        Locker currentLocker = lockManager.getCurrentLocker(target);
        int numLockers = lockManager.getNumLockers(target);
        Integer lockedManually = lockManager.getLockedManually(target);

        if(lockedManually == null && numLockers == 0) {
            TextTools.replyFormatted(ctx, "The %s is $bunlocked", target.getPrettyString());
            return 1;
        } else if(lockedManually != null) {
            StringBuilder sb = new StringBuilder(String.format("The %s is manually locked to $b%s chunks$b", target.getPrettyString(), lockedManually));
            if(numLockers > 0) {
                sb.append(String.format(" and there %s $b%s %s$b queued", (numLockers > 1)? "are" : "is", numLockers, (numLockers > 1)? "lockers" : "locker"));
            }
            TextTools.replyFormatted(ctx, sb.toString());
        } else if(currentLocker != null) {
            StringBuilder sb = new StringBuilder(String.format("The %s is locked to $b%s chunks$b until %s", target.getPrettyString(), currentLocker.getDistance(), currentLocker.getLockedReason()));
            if(numLockers > 1) {
                sb.append(String.format(" and $b%s other %s$b queued", numLockers - 1, (numLockers > 2)? "lockers are" : "locker is"));
            }
            TextTools.replyFormatted(ctx, sb.toString());
        } else {
            TextTools.replyError(ctx, "An error occurred while fetching the lock status");
            return 0;
        }
        return 1;
    }

    private int status(CommandContext<CommandSourceStack> ctx) {
        if(lockStatus(ctx, LockTarget.VIEW) == 1 && lockStatus(ctx, LockTarget.SIM) == 1) {
            return 1;
        }
        return 0;
    }

    private int lock(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");

        lockManager.lockManually(chunks, target);

        replyAndBroadcastLock(ctx, "Locked the %s to $b%s chunks", target.getPrettyString(), chunks);
        return 1;
    }

    private int lockTimeout(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");
        int ticks = IntegerArgumentType.getInteger(ctx, "ticks");

        lockManager.addLocker(new TimeoutLocker(chunks, ticks, target, lockManager));

        replyAndBroadcastLock(ctx, "Locked the %s to $b%s chunks$b for $b%s ticks", target.getPrettyString(), chunks, ticks);
        return 1;
    }

    private int lockPlayerDisconnect(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");
        ServerPlayer player;
        try {
            player = EntityArgument.getPlayer(ctx, "player");
        } catch (CommandSyntaxException e) {
            TextTools.replyError(ctx, "Cannot parse the provided player");
            AdaptiveViewMod.LOGGER.error("Failed to parse player", e);
            return 0;
        }

        lockManager.addLocker(new PlayerDisconnectLocker(player, chunks, target, lockManager));

        replyAndBroadcastLock(ctx, "Locked the %s to $b%s chunks$b until $b%s disconnects", target.getPrettyString(), chunks, player.getName().getString());
        return 1;
    }

    private int lockPlayerMove(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");
        ServerPlayer player;
        try {
            player = EntityArgument.getPlayer(ctx, "player");
        } catch (CommandSyntaxException e) {
            TextTools.replyError(ctx, "Cannot parse the provided player");
            AdaptiveViewMod.LOGGER.error("Failed to parse player", e);
            return 0;
        }

        lockManager.addLocker(new PlayerMoveLocker(player, chunks, target, lockManager));

        replyAndBroadcastLock(ctx, "Locked the %s to $b%s chunks$b until $b%s moves", target.getPrettyString(), chunks, player.getName().getString());
        return 1;
    }

    private int unlock(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        int numLocks = lockManager.getNumLockers(target);
        Integer lockedManually = lockManager.getLockedManually(target);

        if(lockedManually == null) {
            if(numLocks == 0) {
                TextTools.replyFormatted(ctx, "The %s %sn't locked", target.getPrettyString(), target.getIs());
                return 1;
            } else {
                TextTools.replyFormatted(ctx, "The %s %sn't locked manually but there %s %s %s active. Clear them with by appending 'clear' to this command", target.getPrettyString(), target.getIs(), (numLocks > 1)? "are" : "is", numLocks, (numLocks > 1)? "lockers": "locker");
            }
            return 1;
        }

        lockManager.lockManually(null, target);

        if(lockedManually > 0 && numLocks > 0) {
            replyAndBroadcastLock(ctx, "$bUnlocked$b the %s but there %s still $b%s %s$b active", target.getPrettyString(), (numLocks > 1)? "are" : "is", numLocks, (numLocks > 1)? "lockers": "locker");
            return 1;
        }

        replyAndBroadcastLock(ctx, "$bUnlocked$b the %s", target.getPrettyString());
        return 1;
    }

    private int unlockClear(CommandContext<CommandSourceStack> ctx, LockTarget target) {
        int numLocks = lockManager.getNumLockers(target);
        Integer lockedManually = lockManager.getLockedManually(target);

        if(numLocks == 0 && lockedManually == null) {
            TextTools.replyFormatted(ctx, "Nothing to unlock and no lockers to clear");
            return 1;
        }

        lockManager.clearLockers(target);
        lockManager.lockManually(null, target);

        if(lockedManually != null && lockedManually > 0 && numLocks > 0) {
            replyAndBroadcastLock(ctx, "$bUnlocked$b the %s and $bcleared %s %s", target.getPrettyString(), numLocks, (numLocks > 1)? "lockers" : "locker");
            return 1;
        }

        if(lockedManually != null && lockedManually > 0) {
            replyAndBroadcastLock(ctx, "$bUnlocked$b the %s", target.getPrettyString());
            return 1;
        }

        replyAndBroadcastLock(ctx, "$bCleared %s %s", numLocks, (numLocks > 1)? "lockers" : "locker");
        return 1;
    }

    public int allChunks(CommandContext<CommandSourceStack> ctx) {
        return lock(ctx, LockTarget.ALL);
    }

    public int allChunksTimeoutTicks(CommandContext<CommandSourceStack> ctx) {
        return lockTimeout(ctx, LockTarget.ALL);
    }

    public int allChunksPlayerDisconnect(CommandContext<CommandSourceStack> ctx) {
        return lockPlayerDisconnect(ctx, LockTarget.ALL);
    }

    public int allChunksPlayerMove(CommandContext<CommandSourceStack> ctx) {
        return lockPlayerMove(ctx, LockTarget.ALL);
    }

    public int viewChunks(CommandContext<CommandSourceStack> ctx) {
        return lock(ctx, LockTarget.VIEW);
    }

    public int viewChunksTimeoutTicks(CommandContext<CommandSourceStack> ctx) {
        return lockTimeout(ctx, LockTarget.VIEW);
    }

    public int viewChunksPlayerDisconnect(CommandContext<CommandSourceStack> ctx) {
        return lockPlayerDisconnect(ctx, LockTarget.VIEW);
    }

    public int viewChunksPlayerMove(CommandContext<CommandSourceStack> ctx) {
        return lockPlayerMove(ctx, LockTarget.VIEW);
    }

    public int simChunks(CommandContext<CommandSourceStack> ctx) {
        return lock(ctx, LockTarget.SIM);
    }

    public int simChunksTimeoutTicks(CommandContext<CommandSourceStack> ctx) {
        return lockTimeout(ctx, LockTarget.SIM);
    }

    public int simChunksPlayerDisconnect(CommandContext<CommandSourceStack> ctx) {
        return lockPlayerDisconnect(ctx, LockTarget.SIM);
    }

    public int simChunksPlayerMove(CommandContext<CommandSourceStack> ctx) {
        return lockPlayerMove(ctx, LockTarget.SIM);
    }

    public int unlockAll(CommandContext<CommandSourceStack> ctx) {
        return unlock(ctx, LockTarget.ALL);
    }

    public int unlockAllClear(CommandContext<CommandSourceStack> ctx) {
        return unlockClear(ctx, LockTarget.ALL);
    }

    public int unlockView(CommandContext<CommandSourceStack> ctx) {
        return unlock(ctx, LockTarget.VIEW);
    }

    public int unlockViewClear(CommandContext<CommandSourceStack> ctx) {
        return unlockClear(ctx, LockTarget.VIEW);
    }

    public int unlockSim(CommandContext<CommandSourceStack> ctx) {
        return unlock(ctx, LockTarget.SIM);
    }

    public int unlockSimClear(CommandContext<CommandSourceStack> ctx) {
        return unlockClear(ctx, LockTarget.SIM);
    }

    public void registerCommands(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("lock")
                .executes(this::status)
                .then(Commands.literal("status")
                        .executes(this::status)
                )
                .then(Commands.literal("all")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::allChunks)
                                .then(Commands.literal("timeout")
                                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1))
                                                .executes(this::allChunksTimeoutTicks)
                                        )
                                )
                                .then(Commands.literal("player")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.literal("disconnect")
                                                        .executes(this::allChunksPlayerDisconnect)
                                                )
                                                .then(Commands.literal("move")
                                                        .executes(this::allChunksPlayerMove)
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("view")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::viewChunks)
                                .then(Commands.literal("timeout")
                                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1))
                                                .executes(this::viewChunksTimeoutTicks)
                                        )
                                )
                                .then(Commands.literal("player")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.literal("disconnect")
                                                        .executes(this::viewChunksPlayerDisconnect)
                                                )
                                                .then(Commands.literal("move")
                                                        .executes(this::viewChunksPlayerMove)
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("simulation")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::simChunks)
                                .then(Commands.literal("timeout")
                                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1))
                                                .executes(this::simChunksTimeoutTicks)
                                        )
                                )
                                .then(Commands.literal("player")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.literal("disconnect")
                                                        .executes(this::simChunksPlayerDisconnect)
                                                )
                                                .then(Commands.literal("move")
                                                        .executes(this::simChunksPlayerMove)
                                                )
                                        )
                                )
                        )
                )
        )
        .then(Commands.literal("unlock")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .executes(this::status)
                .then(Commands.literal("all")
                        .executes(this::unlockAll)
                        .then(Commands.literal("clear")
                                .executes(this::unlockAllClear)
                        )
                )
                .then(Commands.literal("view")
                        .executes(this::unlockView)
                        .then(Commands.literal("clear")
                                .executes(this::unlockViewClear)
                        )
                )
                .then(Commands.literal("simulation")
                        .executes(this::unlockSim)
                        .then(Commands.literal("clear")
                                .executes(this::unlockSimClear)
                        )
                )
        );
    }
}

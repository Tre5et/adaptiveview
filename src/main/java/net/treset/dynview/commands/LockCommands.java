package net.treset.dynview.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.treset.dynview.config.Config;
import net.treset.dynview.unlocking.LockManager;
import net.treset.dynview.unlocking.LockReason;
import net.treset.dynview.unlocking.ViewDistanceLocker;

public class LockCommands {
    public static int base(CommandContext<ServerCommandSource> ctx) {
        ViewDistanceLocker currentLocker = LockManager.getCurrentLocker();
        int numLockers = LockManager.getNumUnlockers();
        int lockedManually = LockManager.isLockedManually();

        if(Config.getLocked() == 0) {
            ctx.getSource().sendFeedback(Text.literal("The view distance is currently unlocked"), true);
            return 1;
        }

        if(lockedManually > 0) {
            if(numLockers > 0) {
                ctx.getSource().sendFeedback(Text.literal(String.format("The view distance is manually locked to %s chunks and there %s %s %s queued", lockedManually, (numLockers > 1)? "are" : "is", numLockers, (numLockers > 1)? "lockers" : "locker")), true);
            } else ctx.getSource().sendFeedback(Text.literal(String.format("The view distance is manually locked to %s chunks", lockedManually)), true);
            return 1;
        }

        if(currentLocker != null) {
            if(numLockers > 1) {
                ctx.getSource().sendFeedback(Text.literal(String.format("The view distance is locked to %s chunks %s and %s other %s active", currentLocker.getDistance(), currentLocker.getReasonString(), numLockers - 1, (numLockers > 2)? "lockers are" : "locker is")), true);
            } else  ctx.getSource().sendFeedback(Text.literal(String.format("The view distance is locked to %s chunks %s", currentLocker.getDistance(), currentLocker.getReasonString())), true);
            return 1;
        }

        ctx.getSource().sendFeedback(Text.literal(String.format("The view distance is currently locked to %s chunks", Config.getLocked())), true);
        return 1;
    }

    public static int set(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal("Locks the view distance to the provided chunks"), true);
        return 1;
    }

    public static int setChunks(CommandContext<ServerCommandSource> ctx) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");

        LockManager.lockManually(chunks);

        ctx.getSource().sendFeedback(Text.literal(String.format("Locked the view distance to %s chunks", chunks)), true);
        return 1;
    }

    public static int setChunksTimeout(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal("The view distance will be unlocked after the provided amount of ticks"), true);
        return 1;
    }

    public static int setChunksTimeoutTicks(CommandContext<ServerCommandSource> ctx) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");
        int ticks = IntegerArgumentType.getInteger(ctx, "ticks");

        LockManager.addUnlocker(new ViewDistanceLocker(LockReason.TIMEOUT, chunks, ticks, null, ctx));

        ctx.getSource().sendFeedback(Text.literal(String.format("Locked the view distance to %s chunks for %s ticks", chunks, ticks)), true);
        return 1;
    }

    public static int setChunksPlayer(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendFeedback(Text.literal("The view distance will be unlocked after the provided player disconnects or moves"), true);
        return 1;
    }

    public static int setChunksPlayerDisconnect(CommandContext<ServerCommandSource> ctx) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");
        ServerPlayerEntity player;
        try {
            player = EntityArgumentType.getPlayer(ctx, "player");
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.literal("Cannot parse the provided player"));
            e.printStackTrace();
            return 0;
        }

        LockManager.addUnlocker(new ViewDistanceLocker(LockReason.PLAYER_DISCONNECT, chunks, -1, player, ctx));

        ctx.getSource().sendFeedback(Text.literal(String.format("Locked the view distance to %s chunks until player %s disconnects", chunks, player.getName().getString())), true);
        return 1;
    }

    public static int setChunksPlayerMove(CommandContext<ServerCommandSource> ctx) {
        int chunks = IntegerArgumentType.getInteger(ctx, "chunks");
        ServerPlayerEntity player;
        try {
            player = EntityArgumentType.getPlayer(ctx, "player");
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.literal("Cannot parse the provided player"));
            e.printStackTrace();
            return 0;
        }

        LockManager.addUnlocker(new ViewDistanceLocker(LockReason.PLAYER_MOVE, chunks, -1, player, ctx));

        ctx.getSource().sendFeedback(Text.literal(String.format("Locked the view distance to %s chunks until player %s moves", chunks, player.getName().getString())), true);
        return 1;
    }

    public static int clear(CommandContext<ServerCommandSource> ctx) {
        int numLocks = LockManager.getNumUnlockers();
        int lockedManually = LockManager.isLockedManually();

        if(numLocks == 0) {
            ctx.getSource().sendFeedback(Text.literal("No unlockers are queued to clear"), true);
            return 1;
        }

        LockManager.clear();

        if(lockedManually > 0 && numLocks > 0) {
            ctx.getSource().sendFeedback(Text.literal(String.format("Cleared %s unlockers but view distance is still manually locked to %s chunks", numLocks, lockedManually)), true);
            return 1;
        }

        ctx.getSource().sendFeedback(Text.literal(String.format("Cleared %s unlockers", numLocks)), true);
        return 1;
    }

    public static int unlock(CommandContext<ServerCommandSource> ctx) {
        int numLocks = LockManager.getNumUnlockers();
        int lockedManually = LockManager.isLockedManually();

        if(lockedManually == 0) {
            ctx.getSource().sendFeedback(Text.literal("The view distance isn't manually locked"), true);
        }

        LockManager.unlockManually();

        if(lockedManually > 0 && numLocks > 0) {
            ctx.getSource().sendFeedback(Text.literal(String.format("Unlocked manually but there %s still %s %s active", (numLocks > 1)? "are" : "is", numLocks, (numLocks > 1)? "lockers": "locker")), true);
            return 1;
        }

        ctx.getSource().sendFeedback(Text.literal("Unlocked the view distance"), true);
        return 1;
    }
}

package net.treset.adaptiveview.unlocking;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.treset.adaptiveview.tools.TextTools;

public class ViewDistanceLocker {
    private final LockReason lockReason;
    private final int distance;
    private final int timeout;
    private int remainingTime;
    private final ServerPlayerEntity player;
    private Vec3d startPos;
    private final CommandContext<ServerCommandSource> ctx;

    public ViewDistanceLocker(LockReason lockReason, int distance, int timeout, ServerPlayerEntity player, CommandContext<ServerCommandSource> ctx) {
        this.lockReason = lockReason;
        this.distance = distance;
        this.timeout = this.remainingTime = timeout;
        this.player = player;
        this.ctx = ctx;

        if(this.getUnlockReason() == LockReason.PLAYER_MOVE) {
            this.startPos = player.getPos();
        }
    }

    public LockReason getUnlockReason() { return lockReason; }
    public int getDistance() { return distance; }
    public int getTimeout() { return timeout; }

    public void onTick() {
        if(this.getUnlockReason() == LockReason.TIMEOUT) {
            this.remainingTime--;
            if(this.remainingTime <= 0) {
                LockManager.finishUnlocker(this);
                TextTools.replyFormatted(ctx, String.format("?aCleared view distance lock of ?B%s chunks?B after ?B%s ticks", this.getDistance(), this.getTimeout()), true);
            }
        } else if(this.getUnlockReason() == LockReason.PLAYER_DISCONNECT) {
             if(this.player.isDisconnected()) {
                 LockManager.finishUnlocker(this);
                 TextTools.replyFormatted(ctx, String.format("?aCleared view distance lock of ?B%s chunks?B after ?Bplayer %s disconnected", this.getDistance(), this.player.getName().getString()), true);
             }
        } else if(this.getUnlockReason() == LockReason.PLAYER_MOVE) {
            if(this.player.isDisconnected() || this.player.getPos() != this.startPos) {
                LockManager.finishUnlocker(this);
                TextTools.replyFormatted(ctx, String.format("?aCleared view distance lock of ?B%s chunks?B after ?Bplayer %s moved", this.getDistance(), this.player.getName().getString()), true);
            }
        }
    }

    public String getReasonString() {
        if(this.getUnlockReason() == LockReason.TIMEOUT) {
            return String.format("until %s ticks have passed.", this.timeout);
        } else if(this.getUnlockReason() == LockReason.PLAYER_DISCONNECT) {
            return String.format("until player %s disconnects", this.player.getName().getString());
        } else if(this.getUnlockReason() == LockReason.PLAYER_MOVE) {
            return String.format("until player %s moves", this.player.getName().getString());
        }
        return "";
    }
}

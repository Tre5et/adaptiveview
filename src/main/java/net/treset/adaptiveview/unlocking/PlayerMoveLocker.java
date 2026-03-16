package net.treset.adaptiveview.unlocking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.treset.adaptiveview.tools.Message;

public class PlayerMoveLocker extends Locker {
    private final ServerPlayer player;
    private final Vec3 startPos;

    public PlayerMoveLocker(ServerPlayer player, int distance, LockTarget target, LockManager lockManager) {
        super(distance, target, lockManager);
        this.player = player;
        this.startPos = player.position();
    }

    @Override
    public boolean shouldUnlock() {
        return this.player.hasDisconnected() || !this.player.position().equals(this.startPos);
    }

    @Override
    public Message getUnlockReason() {
        return new Message("$b%s moved", this.player.getName().getString());
    }

    @Override
    public Message getLockedReason() {
        return new Message("$b%s moves", this.player.getName().getString());
    }
}

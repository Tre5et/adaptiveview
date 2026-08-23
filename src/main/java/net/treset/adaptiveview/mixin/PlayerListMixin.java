package net.treset.adaptiveview.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    // we just prevent the ClientboundSetChunkCacheRadiusPacket from being sent to the players
    @Redirect(method = "setViewDistance",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"
            ))
    public void redirectBroadcastAll(PlayerList instance, Packet<?> packet) {}
}

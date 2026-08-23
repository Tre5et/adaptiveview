package net.treset.adaptiveview.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.Target;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    private int viewDistance;

    @Shadow
    public abstract void broadcastAll(Packet<?> packet);

    @Inject(method = "setViewDistance",at = @At("HEAD"), cancellable = true)
    public void setViewDistance(int viewDistance, CallbackInfo ci) {;
        this.viewDistance = viewDistance;
        // this.broadcastAll(new ClientboundSetChunkCacheRadiusPacket(viewDistance));

        for(ServerLevel level : this.server.getAllLevels()) {
            level.getChunkSource().setViewDistance(viewDistance);
        }
        ci.cancel();
    }
}

package net.treset.adaptiveview.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.treset.adaptiveview.AdaptiveViewMod;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method="tickServer(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci, @Local(ordinal = 1) long tickTime) {
        AdaptiveViewMod.getServerHandler().getTickLengths().add(tickTime);
    }
}

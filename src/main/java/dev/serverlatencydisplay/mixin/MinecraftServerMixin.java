package dev.serverlatencydisplay.mixin;

import dev.serverlatencydisplay.ServerLatencyDisplay;
import java.util.function.BooleanSupplier;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin {
    @Unique
    private int serverLatencyDisplay$ticksUntilUpdate = ServerLatencyDisplay.UPDATE_INTERVAL_TICKS;

    @Inject(method = "tickServer", at = @At("TAIL"))
    private void serverLatencyDisplay$showLatency(BooleanSupplier hasTimeLeft, CallbackInfo callbackInfo) {
        if (--serverLatencyDisplay$ticksUntilUpdate > 0) {
            return;
        }

        serverLatencyDisplay$ticksUntilUpdate = ServerLatencyDisplay.UPDATE_INTERVAL_TICKS;
        ServerLatencyDisplay.showLatency((MinecraftServer) (Object) this);
    }
}

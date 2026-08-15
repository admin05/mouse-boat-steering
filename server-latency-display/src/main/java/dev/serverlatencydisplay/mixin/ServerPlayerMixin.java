package dev.serverlatencydisplay.mixin;

import dev.serverlatencydisplay.ServerLatencyDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    private void serverLatencyDisplay$appendStats(CallbackInfoReturnable<Component> callbackInfo) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        Component baseName = callbackInfo.getReturnValue();
        if (baseName == null) {
            baseName = Component.literal(player.getGameProfile().name());
        }

        callbackInfo.setReturnValue(ServerLatencyDisplay.createTabListDisplayName(server, player, baseName));
    }
}

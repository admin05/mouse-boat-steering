package dev.mouseboatsteering.mixin;

import dev.mouseboatsteering.FrostWalkerControl;
import dev.mouseboatsteering.network.ToggleFrostWalkerPayload;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
abstract class ServerGamePacketListenerMixin {
    @Shadow
    public net.minecraft.server.level.ServerPlayer player;

    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void mouseBoatSteering$handleTogglePacket(
            ServerboundCustomPayloadPacket packet,
            CallbackInfo callbackInfo
    ) {
        if (packet.payload().type().id().equals(ToggleFrostWalkerPayload.TYPE.id())) {
            FrostWalkerControl.toggle(player);
        }
    }
}

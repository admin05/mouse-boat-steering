package dev.mouseboatsteering;

import dev.mouseboatsteering.network.ToggleFrostWalkerPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class MouseBoatSteering implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(
                ToggleFrostWalkerPayload.TYPE,
                ToggleFrostWalkerPayload.STREAM_CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                ToggleFrostWalkerPayload.TYPE,
                (payload, context) -> FrostWalkerControl.toggle(context.player())
        );
    }
}

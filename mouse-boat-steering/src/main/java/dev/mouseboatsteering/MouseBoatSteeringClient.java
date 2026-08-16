package dev.mouseboatsteering;

import dev.mouseboatsteering.network.ToggleFrostWalkerPayload;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

public final class MouseBoatSteeringClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }

    public static void sendTogglePacket() {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && client.getConnection() != null) {
            client.getConnection().getConnection().send(
                    new ServerboundCustomPayloadPacket(
                            new DiscardedPayload(ToggleFrostWalkerPayload.TYPE.id())
                    )
            );
        }
    }
}

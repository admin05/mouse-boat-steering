package dev.mouseboatsteering.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ToggleFrostWalkerPayload() implements CustomPacketPayload {
    public static final ToggleFrostWalkerPayload INSTANCE = new ToggleFrostWalkerPayload();
    public static final Type<ToggleFrostWalkerPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(
                    "mouse_boat_steering",
                    "toggle_frost_walker"
            ));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleFrostWalkerPayload> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

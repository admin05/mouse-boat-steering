package dev.mouseboatsteering;

import com.mojang.blaze3d.platform.InputConstants;
import dev.mouseboatsteering.network.ToggleFrostWalkerPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class MouseBoatSteeringClient implements ClientModInitializer {
    private static final KeyMapping TOGGLE_FROST_WALKER = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.mouse_boat_steering.toggle_frost_walker",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_C,
                    KeyMapping.Category.GAMEPLAY
            )
    );

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_FROST_WALKER.consumeClick()) {
                if (client.player != null && ClientPlayNetworking.canSend(ToggleFrostWalkerPayload.TYPE)) {
                    ClientPlayNetworking.send(ToggleFrostWalkerPayload.INSTANCE);
                }
            }
        });
    }
}

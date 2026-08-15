package dev.mouseboatsteering.mixin;

import dev.mouseboatsteering.MouseBoatSteeringClient;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
abstract class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void mouseBoatSteering$handleToggleKey(long window, int action, KeyEvent event, CallbackInfo callbackInfo) {
        if (action == GLFW.GLFW_PRESS && event.key() == GLFW.GLFW_KEY_C) {
            MouseBoatSteeringClient.sendTogglePacket();
        }
    }
}

package dev.mouseboatsteering.mixin;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
abstract class LocalPlayerMixin {
    @Shadow
    public ClientInput input;

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void mouseBoatSteering$applyMouseHeading(CallbackInfo callbackInfo) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        Entity controlledVehicle = player.getControlledVehicle();

        if (!(controlledVehicle instanceof AbstractBoat boat)) {
            return;
        }

        Input keys = input.keyPresses;
        boat.setInput(false, false, keys.forward(), keys.backward());
        boat.setYRot(player.getYRot());
    }
}

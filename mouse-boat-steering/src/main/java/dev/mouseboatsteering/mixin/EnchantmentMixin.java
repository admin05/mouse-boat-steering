package dev.mouseboatsteering.mixin;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
abstract class EnchantmentMixin {
    @Unique
    private static final String MOUSE_BOAT_STEERING$FROST_WALKER_KEY =
            "enchantment.minecraft.frost_walker";

    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
    private void mouseBoatSteering$setFrostWalkerMaxLevel(CallbackInfoReturnable<Integer> callbackInfo) {
        if (mouseBoatSteering$isFrostWalker()) {
            callbackInfo.setReturnValue(4);
        }
    }

    @Inject(method = "runLocationChangedEffects", at = @At("HEAD"), cancellable = true)
    private void mouseBoatSteering$disableVanillaIceForHigherLevels(
            ServerLevel level,
            int enchantmentLevel,
            EnchantedItemInUse enchantedItem,
            LivingEntity entity,
            CallbackInfo callbackInfo
    ) {
        if (enchantmentLevel >= 1 && mouseBoatSteering$isFrostWalker()) {
            callbackInfo.cancel();
        }
    }

    @Unique
    private boolean mouseBoatSteering$isFrostWalker() {
        Enchantment enchantment = (Enchantment) (Object) this;
        return enchantment.description().getContents() instanceof TranslatableContents contents
                && MOUSE_BOAT_STEERING$FROST_WALKER_KEY.equals(contents.getKey());
    }
}

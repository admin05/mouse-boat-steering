package dev.mouseboatsteering.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
    @Unique
    private static final Logger MOUSE_BOAT_STEERING$LOGGER =
            LoggerFactory.getLogger("mouse_boat_steering");

    @Unique
    private static boolean mouseBoatSteering$loggedFrostWalkerRuntime;

    @Unique
    private long mouseBoatSteering$lastFreezeCenter = Long.MIN_VALUE;

    @Unique
    private int mouseBoatSteering$lastFrostWalkerLevel;

    @Inject(method = "tick", at = @At("TAIL"))
    private void mouseBoatSteering$freezeWaterForHigherLevels(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
        if (!EnchantmentHelper.hasAnyEnchantments(boots)) {
            mouseBoatSteering$lastFrostWalkerLevel = 0;
            return;
        }

        Holder<Enchantment> frostWalker = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FROST_WALKER);
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(frostWalker, boots);
        if (enchantmentLevel > 0 && !mouseBoatSteering$loggedFrostWalkerRuntime) {
            mouseBoatSteering$loggedFrostWalkerRuntime = true;
            MOUSE_BOAT_STEERING$LOGGER.info(
                    "Frost Walker+ runtime active: detected level {}, minimum level {}, maximum level {}",
                    enchantmentLevel,
                    frostWalker.value().getMinLevel(),
                    frostWalker.value().getMaxLevel()
            );
        }

        Block ice = switch (enchantmentLevel) {
            case 1 -> Blocks.FROSTED_ICE;
            case 2 -> Blocks.ICE;
            case 3 -> Blocks.PACKED_ICE;
            case 4 -> Blocks.BLUE_ICE;
            default -> null;
        };

        if (ice == null) {
            mouseBoatSteering$lastFrostWalkerLevel = enchantmentLevel;
            return;
        }

        boolean requireOriginalConditions = enchantmentLevel == 1;
        if (requireOriginalConditions && (!entity.onGround() || entity.isPassenger())) {
            return;
        }

        BlockPos center = entity.blockPosition().below();
        long packedCenter = center.asLong();
        if (packedCenter == mouseBoatSteering$lastFreezeCenter
                && enchantmentLevel == mouseBoatSteering$lastFrostWalkerLevel) {
            return;
        }

        mouseBoatSteering$lastFreezeCenter = packedCenter;
        mouseBoatSteering$lastFrostWalkerLevel = enchantmentLevel;
        freezeWaterDisk(
                level,
                center,
                ice.defaultBlockState(),
                enchantmentLevel + 2,
                requireOriginalConditions
        );
    }

    @Unique
    private static void freezeWaterDisk(
            ServerLevel level,
            BlockPos center,
            BlockState ice,
            int radius,
            boolean requireAirAbove
    ) {
        int radiusSquared = radius * radius;

        for (BlockPos position : BlockPos.betweenClosed(
                center.offset(-radius, 0, -radius),
                center.offset(radius, 0, radius)
        )) {
            int deltaX = position.getX() - center.getX();
            int deltaZ = position.getZ() - center.getZ();
            if (deltaX * deltaX + deltaZ * deltaZ > radiusSquared) {
                continue;
            }

            if (level.getBlockState(position).is(Blocks.WATER)
                    && (!requireAirAbove || level.getBlockState(position.above()).isAir())) {
                level.setBlockAndUpdate(position, ice);
            }
        }
    }
}

package dev.mouseboatsteering;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.Equippable;

public final class FrostWalkerControl {
    private static final Set<UUID> DISABLED_PLAYERS = new HashSet<>();

    private FrostWalkerControl() {
    }

    public static boolean isIceGenerationEnabled(LivingEntity entity) {
        return !(entity instanceof Player player) || !DISABLED_PLAYERS.contains(player.getUUID());
    }

    public static void toggle(ServerPlayer player) {
        if (!isHoldingFrostWalkerBoots(player)) {
            player.displayClientMessage(
                    Component.translatable("message.mouse_boat_steering.hold_frost_walker_boots"),
                    true
            );
            return;
        }

        boolean enabled;
        if (DISABLED_PLAYERS.remove(player.getUUID())) {
            enabled = true;
        } else {
            DISABLED_PLAYERS.add(player.getUUID());
            enabled = false;
        }

        player.displayClientMessage(
                Component.translatable(enabled
                        ? "message.mouse_boat_steering.ice_enabled"
                        : "message.mouse_boat_steering.ice_disabled"),
                true
        );
    }

    private static boolean isHoldingFrostWalkerBoots(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
            if (equippable != null
                    && equippable.slot() == EquipmentSlot.FEET
                    && stack.getEnchantments().keySet().stream().anyMatch(holder -> holder.is(Enchantments.FROST_WALKER))) {
                return true;
            }
        }

        return false;
    }
}

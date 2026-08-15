package dev.serverlatencydisplay;

import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class ServerLatencyDisplay implements ModInitializer {
    public static final int UPDATE_INTERVAL_TICKS = 20;

    @Override
    public void onInitialize() {
    }

    public static void showLatency(MinecraftServer server) {
        if (server.getPlayerList() == null) {
            return;
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            int latency = Math.max(0, player.connection.latency());
            player.displayClientMessage(createLatencyMessage(latency), true);
        }
    }

    private static Component createLatencyMessage(int latency) {
        ChatFormatting latencyColor;
        if (latency < 100) {
            latencyColor = ChatFormatting.GREEN;
        } else if (latency < 200) {
            latencyColor = ChatFormatting.YELLOW;
        } else {
            latencyColor = ChatFormatting.RED;
        }

        return Component.literal("延迟: ")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(latency + " ms").withStyle(latencyColor));
    }
}

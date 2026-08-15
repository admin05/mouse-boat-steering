package dev.serverlatencydisplay;

import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.EnumSet;
import java.util.List;

public final class ServerLatencyDisplay implements ModInitializer {
    public static final int UPDATE_INTERVAL_TICKS = 20;

    @Override
    public void onInitialize() {
    }

    public static void updateTabList(MinecraftServer server) {
        if (server.getPlayerList() == null) {
            return;
        }

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) {
            return;
        }

        server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(
                EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME),
                players
        ));
    }

    public static Component createTabListDisplayName(MinecraftServer server, ServerPlayer player, Component baseName) {
        int latency = Math.max(0, player.connection.latency());
        double tps = calculateTps(server);

        return baseName.copy()
                .append(Component.literal("  | Ping: ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(latency + " ms").withStyle(latencyColor(latency)))
                .append(Component.literal(" | TPS: ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(String.format(java.util.Locale.ROOT, "%.2f", tps))
                        .withStyle(tpsColor(tps)));
    }

    private static double calculateTps(MinecraftServer server) {
        long averageTickNanos = server.getAverageTickTimeNanos();
        if (averageTickNanos <= 0L) {
            return 20.0;
        }

        return Math.min(20.0, 1_000_000_000.0 / averageTickNanos);
    }

    private static ChatFormatting latencyColor(int latency) {
        ChatFormatting latencyColor;
        if (latency < 100) {
            latencyColor = ChatFormatting.GREEN;
        } else if (latency < 200) {
            latencyColor = ChatFormatting.YELLOW;
        } else {
            latencyColor = ChatFormatting.RED;
        }

        return latencyColor;
    }

    private static ChatFormatting tpsColor(double tps) {
        if (tps >= 19.0) {
            return ChatFormatting.GREEN;
        }
        if (tps >= 15.0) {
            return ChatFormatting.YELLOW;
        }
        return ChatFormatting.RED;
    }
}

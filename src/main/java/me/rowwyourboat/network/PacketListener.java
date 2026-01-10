package me.rowwyourboat.network;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.network.payloads.MarkPlayerC2SPayload;
import me.rowwyourboat.network.payloads.SparkAbilityC2SPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PacketListener {

    public static void register() {
        PayloadTypeRegistry.playC2S().register(SparkAbilityC2SPayload.ID, SparkAbilityC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(MarkPlayerC2SPayload.ID, MarkPlayerC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SparkAbilityC2SPayload.ID, PacketListener::onSparkAbilityPayload);
        ServerPlayNetworking.registerGlobalReceiver(MarkPlayerC2SPayload.ID, PacketListener::onMarkPlayerPayload);
    }

    private static void onSparkAbilityPayload(SparkAbilityC2SPayload payload, ServerPlayNetworking.Context context) {
        Matchbox.LOGGER.info("Received player: {}", context.player().getName());
    }

    private static void onMarkPlayerPayload(MarkPlayerC2SPayload payload, ServerPlayNetworking.Context context) {
        Matchbox.LOGGER.info("Received target uuid: {}", payload.targetId);
    }

}

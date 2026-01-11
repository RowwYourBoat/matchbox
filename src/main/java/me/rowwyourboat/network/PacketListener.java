package me.rowwyourboat.network;

import me.rowwyourboat.game.GameInstance;
import me.rowwyourboat.network.payloads.MarkPlayerC2SPayload;
import me.rowwyourboat.network.payloads.SparkAbilityC2SPayload;
import me.rowwyourboat.services.GameService;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.UUID;

public class PacketListener {

    public static void register() {
        PayloadTypeRegistry.playC2S().register(SparkAbilityC2SPayload.ID, SparkAbilityC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(MarkPlayerC2SPayload.ID, MarkPlayerC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SparkAbilityC2SPayload.ID, PacketListener::onSparkAbilityPayload);
        ServerPlayNetworking.registerGlobalReceiver(MarkPlayerC2SPayload.ID, PacketListener::onMarkPlayerPayload);
    }

    private static void onSparkAbilityPayload(SparkAbilityC2SPayload payload, ServerPlayNetworking.Context context) {
        GameInstance game = GameService.getGameFromWorld(context.player().getEntityWorld());
        if (game == null) { return; }
        game.performSparkSwapAbility(context.player().getUuid());
    }

    private static void onMarkPlayerPayload(MarkPlayerC2SPayload payload, ServerPlayNetworking.Context context) {
        UUID targetId = payload.targetId.orElse(null);
        if (targetId == null) { return; }

        GameInstance game = GameService.getGameFromWorld(context.player().getEntityWorld());
        if (game == null) { return; }

        game.setTargetAsMarkedBy(targetId, context.player().getUuid());
    }

}

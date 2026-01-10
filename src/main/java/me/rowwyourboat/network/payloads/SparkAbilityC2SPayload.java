package me.rowwyourboat.network.payloads;

import me.rowwyourboat.Matchbox;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Sent when the spark presses their ability keybinding
 */
public class SparkAbilityC2SPayload implements CustomPayload {
    // Payload needs a single instance
    public static final SparkAbilityC2SPayload INSTANCE = new SparkAbilityC2SPayload();
    public static final Id<SparkAbilityC2SPayload> ID = new CustomPayload.Id<>(Identifier.of(Matchbox.MOD_ID, "spark_ability_c2s"));
    public static final PacketCodec<PacketByteBuf, SparkAbilityC2SPayload> CODEC = PacketCodec.unit(INSTANCE);

    private SparkAbilityC2SPayload() { }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

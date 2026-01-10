package me.rowwyourboat.network.payloads;

import me.rowwyourboat.Matchbox;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.Optional;
import java.util.UUID;

/**
 * Sent when the spark or medic right-clicks a player
 */
public class MarkPlayerC2SPayload implements CustomPayload {
    public static final Id<MarkPlayerC2SPayload> ID = new Id<>(Identifier.of(Matchbox.MOD_ID, "mark_player_c2s"));
    public static final PacketCodec<PacketByteBuf, MarkPlayerC2SPayload> CODEC = CustomPayload.codecOf(MarkPlayerC2SPayload::write, MarkPlayerC2SPayload::new);

    public Optional<UUID> targetId;

    public MarkPlayerC2SPayload(Optional<UUID> targetUuid) {
        this.targetId = targetUuid;
    }

    private MarkPlayerC2SPayload(PacketByteBuf buf) {
        this(buf.readOptional(Uuids.PACKET_CODEC));
    }

    private void write(PacketByteBuf buf) {
        buf.writeOptional(this.targetId, Uuids.PACKET_CODEC);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package me.rowwyourboat.managers;

import com.mojang.serialization.Codec;
import me.rowwyourboat.data.SpawnLocations;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentStateType;

public class DataManager {

    private static final Codec<SpawnLocations> SPAWN_CODEC = Codec.list(BlockPos.CODEC).xmap(SpawnLocations::new, SpawnLocations::getBlockPosList);

    private static final PersistentStateType<SpawnLocations> SPAWN_STATE_TYPE = new PersistentStateType<>(
            "spawn_locations",
            SpawnLocations::new,
            SPAWN_CODEC,
            null
    );

    public static SpawnLocations getGlobalSpawnLocations(MinecraftServer server) {
        return server.getSpawnWorld().getPersistentStateManager().getOrCreate(SPAWN_STATE_TYPE);
    }

}

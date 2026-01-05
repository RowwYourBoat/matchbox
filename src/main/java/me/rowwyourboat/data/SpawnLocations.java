package me.rowwyourboat.data;

import com.mojang.serialization.Codec;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.ArrayList;
import java.util.List;

public class SpawnLocations extends PersistentState {

    private List<BlockPos> spawnLocations;

    public SpawnLocations(List<BlockPos> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public SpawnLocations() {
        this(new ArrayList<>());
    }

    public List<BlockPos> getList() {
        return this.spawnLocations;
    }

    public void add(BlockPos pos) {
        List<BlockPos> spawnLocations = new ArrayList<>(this.spawnLocations);
        spawnLocations.add(pos);

        this.spawnLocations = spawnLocations;
        super.markDirty();
    }

    public void remove(int index) {
        List<BlockPos> spawnLocations = new ArrayList<>(this.spawnLocations);
        spawnLocations.remove(index);

        this.spawnLocations = spawnLocations;
        super.markDirty();
    }

    public void clear() {
        List<BlockPos> spawnLocations = new ArrayList<>(this.spawnLocations);
        spawnLocations.clear();

        this.spawnLocations = spawnLocations;
        super.markDirty();
    }

    private static final Codec<SpawnLocations> CODEC = Codec.list(BlockPos.CODEC).xmap(SpawnLocations::new, SpawnLocations::getList);

    private static final PersistentStateType<SpawnLocations> STATE_TYPE = new PersistentStateType<>(
            "spawn_locations",
            SpawnLocations::new,
            CODEC,
            DataFixTypes.SAVED_DATA_WORLD_BORDER
    );

    public static SpawnLocations getSpawnLocationsData(MinecraftServer server) {
        return server.getSpawnWorld().getPersistentStateManager().getOrCreate(STATE_TYPE);
    }

}

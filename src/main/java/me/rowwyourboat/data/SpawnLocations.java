package me.rowwyourboat.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

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

    public List<BlockPos> getBlockPosList() {
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

}

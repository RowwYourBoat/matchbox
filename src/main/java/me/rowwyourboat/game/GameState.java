package me.rowwyourboat.game;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.UUID;

public class GameState {

    private final ServerWorld world;
    private final Timer timer;

    private GamePhase phase;
    private CounterTask counter;
    private long phaseTimeLeft;

    private UUID sparkId;
    private UUID medicId;

    public GameState(ServerWorld world) {
        this.world = world;
        this.timer = new Timer();

        this.phase = GamePhase.SETUP;
    }

    public void purge() {
        this.timer.cancel();
    }

    public void onTimerEnded() {

    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public void decrementPhaseTimeLeft() {
        this.phaseTimeLeft--;
    }

    public void setPhaseTimeLeft(long time) {
        this.phaseTimeLeft = time;
    }

    public long getPhaseTimeLeft() {
        return this.phaseTimeLeft;
    }

    public GamePhase getPhase() {
        return this.phase;
    }

    public RegistryKey<World> getIdentifier() {
        return this.world.getRegistryKey();
    }

    public ServerWorld getGameWorld() {
        return this.world;
    }
}

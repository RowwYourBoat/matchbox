package me.rowwyourboat.game;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.managers.TeamManager;
import me.rowwyourboat.utils.enums.GamePhase;
import me.rowwyourboat.utils.enums.Role;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/*
 * Round start:
 * 1. Disable chat
 * 2. Spread candidates out in the map (start with blind- and slowness)
 * 3. Assign roles
 * 4. Start a 10-minute round duration timer
 */

/*
 * End of round (meeting):
 * 1. Player marked by the spark dies (unless they're marked by the medic in which case they are told)
 * 2. All candidates get blind- and slowness
 * 3. Enable chat
 * 4. Start a 10-minute meeting duration timer (3:30 for first round)
 * 5. Disable chat when the timer hits 0
 * 6. Show voting UI
 */

public class GameInstance {

    private final static HashMap<RegistryKey<World>, GameInstance> instances = new HashMap<>();

    private final ServerWorld world;
    public final PlayerManager playerManager;
    private final HashMap<Role, UUID> markedByMap;
    public final List<ScheduledAction> scheduledActions;

    private GamePhase phase;

    public static void create(ServerWorld world) {
        GameInstance game = new GameInstance(world);
        instances.put(world.getRegistryKey(), game);
    }

    public static void remove(GameInstance game) {
        game.end();
        instances.remove(game.getIdentifier());
    }

    @Nullable
    public static GameInstance getByWorld(ServerWorld world) {
        return instances.get(world.getRegistryKey());
    }

    public GameInstance(ServerWorld world) {
        this.world = world;
        this.markedByMap = new HashMap<>();
        this.playerManager = new PlayerManager(world);
        this.scheduledActions = new ArrayList<>();

        this.performSetup();
    }

    private void tick(MinecraftServer server) {
        List<ScheduledAction> remainingActions = new ArrayList<>();

        for (ScheduledAction action : this.scheduledActions) {
            if (action.hasExecuted()) { continue; }

            remainingActions.add(action);
            action.tick();
        }

        this.scheduledActions.clear();
        this.scheduledActions.addAll(remainingActions);
    }

    private void performSetup() {
        this.phase = GamePhase.SETUP;
        this.registerEvents();
        this.playerManager.performSetup();

        this.scheduledActions.add(new ScheduledAction(this, 30, this.playerManager::assignSpecialRoles));
    }

    private void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(this::tick);
    }

    public void end() {
        TeamManager.clearTeams();
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public void performSparkSwapAbility(UUID playerId) {

    }

    public void setTargetAsMarkedBy(UUID targetId, UUID markerId) {
        Role targetRole = this.playerManager.getRole(targetId);
        Role markerRole = this.playerManager.getRole(markerId);
        if (
            (targetRole == null || targetRole == Role.SPARK) ||
            (markerRole == null || markerRole == Role.STANDARD)
        ) { return; }

        ServerPlayerEntity targetPlayer = this.playerManager.getById(targetId);
        ServerPlayerEntity markerPlayer = this.playerManager.getById(markerId);
        if (targetPlayer == null || markerPlayer == null) { return; }

        if (!markerPlayer.isInRange(targetPlayer, Matchbox.markRange)) { return; }

        switch (markerRole) {
            case SPARK: {
                this.markedByMap.putIfAbsent(Role.SPARK, targetId);
                break;
            } case MEDIC: {
                this.markedByMap.putIfAbsent(Role.MEDIC, targetId);
                break;
            }
        }
    }

    public GamePhase getPhase() {
        return this.phase;
    }

    public RegistryKey<World> getIdentifier() {
        return this.world.getRegistryKey();
    }

    public ServerWorld getWorld() {
        return this.world;
    }
}

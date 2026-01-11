package me.rowwyourboat.game;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.game.enums.GamePhase;
import me.rowwyourboat.game.enums.Role;
import me.rowwyourboat.services.NameVisibilityService;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class GameInstance {

    private final ServerWorld world;
    private final HashMap<UUID, Role> playerRoleMap;

    private final Timer timer;
    private GamePhase phase;
    private CounterTask counter;

    private long phaseTimeLeft;
    private long sparkAbilityCooldown;

    private UUID markedBySparkId;
    private UUID markedByMedicId;

    public GameInstance(ServerWorld world) {
        this.world = world;

        this.playerRoleMap = new HashMap<>();
        for (ServerPlayerEntity player : world.getPlayers()) {
            this.playerRoleMap.put(player.getUuid(), Role.STANDARD);
        }

        this.timer = new Timer();
        this.phase = GamePhase.SETUP;
    }

    public void purge() {
        this.timer.cancel();
        NameVisibilityService.clearTeams();
    }

    public void onTimerEnded() {

    }

    public void decrementPhaseTimeLeft() {
        this.phaseTimeLeft--;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public void setPlayerRole(ServerPlayerEntity player, Role role) {
        if (!role.canHaveMultiple() && !this.getPlayersByRole(role).isEmpty()) {
            Matchbox.LOGGER.warn("Tried to assign {} on a game instance with one already present!", role.getName());
            return;
        }

        this.playerRoleMap.put(player.getUuid(), role);
    }

    public void performSparkSwapAbility(UUID playerId) {

    }

    public void setTargetAsMarkedBy(UUID targetId, UUID markerId) {
        Role targetRole = this.playerRoleMap.get(targetId);
        Role markerRole = this.playerRoleMap.get(markerId);
        if (
            (targetRole == null || targetRole == Role.SPARK) ||
            (markerRole == null || markerRole == Role.STANDARD)
        ) { return; }

        ServerPlayerEntity targetPlayer = this.getPlayerById(targetId);
        ServerPlayerEntity markerPlayer = this.getPlayerById(markerId);
        if (targetPlayer == null || markerPlayer == null) { return; }

        if (!markerPlayer.isInRange(targetPlayer, Matchbox.markRange)) { return; }

        switch (markerRole) {
            case SPARK: {
                if (this.markedBySparkId != null) { break; }
                this.markedBySparkId = targetId;
                break;
            } case MEDIC: {
                if (this.markedByMedicId != null) { break; }
                this.markedByMedicId = targetId;
                break;
            }
        }
    }

    public void setPhaseTimeLeft(long time) {
        this.phaseTimeLeft = time;
    }

    public List<ServerPlayerEntity> getAllPlayers() {
        List<ServerPlayerEntity> players = new ArrayList<>();

        for (UUID uuid : playerRoleMap.keySet()) {
            players.add((ServerPlayerEntity) this.world.getPlayerByUuid(uuid));
        }

        return players;
    }

    public List<ServerPlayerEntity> getPlayers(Predicate<ServerPlayerEntity> predicate) {
        List<ServerPlayerEntity> players = new ArrayList<>(this.world.getPlayers());
        players.removeIf(predicate.negate());

        return new ArrayList<>(players);
    }

    public long getPhaseTimeLeft() {
        return this.phaseTimeLeft;
    }

    public GamePhase getPhase() {
        return this.phase;
    }

    public @Nullable Role getPlayerRole(ServerPlayerEntity player) {
        return this.playerRoleMap.get(player.getUuid());
    }

    public @Nullable ServerPlayerEntity getPlayerById(UUID id) {
        return this.world.getPlayers(plr -> plr.getUuid().equals(id)).getFirst();
    }

    public List<ServerPlayerEntity> getPlayersByRole(Role role) {
        List<ServerPlayerEntity> players = new ArrayList<>();

        for (UUID uuid : playerRoleMap.keySet()) {
            Role playerRole = this.playerRoleMap.get(uuid);
            if (!playerRole.equals(role)) { continue; }
            players.add((ServerPlayerEntity) this.world.getPlayerByUuid(uuid));
        }

        return players;
    }

    public RegistryKey<World> getIdentifier() {
        return this.world.getRegistryKey();
    }

    public ServerWorld getGameWorld() {
        return this.world;
    }
}

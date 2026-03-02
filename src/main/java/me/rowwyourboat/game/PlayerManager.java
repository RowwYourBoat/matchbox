package me.rowwyourboat.game;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.managers.DataManager;
import me.rowwyourboat.managers.TeamManager;
import me.rowwyourboat.utils.enums.Role;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class PlayerManager {

    private final ServerWorld world;
    private final HashMap<UUID, Role> roleMap;

    public PlayerManager(ServerWorld world) {
        this.world = world;
        this.roleMap = new HashMap<>();

        this.performSetup();
    }

    public void performSetup() {
        this.randomizeLocations();
        this.fillRoleMap();
    }

    public void fillRoleMap() {
        for (ServerPlayerEntity player : world.getPlayers()) {
            this.roleMap.put(player.getUuid(), Role.STANDARD);
        }
    }

    public void setRole(ServerPlayerEntity player, Role role) {
        if (role.isSpecial() && !this.getAllByRole(role).isEmpty()) {
            Matchbox.LOGGER.warn("Tried to assign {} on a game instance with one already present!", role.getName());
            return;
        }

        this.roleMap.put(player.getUuid(), role);
    }

    private void hideAllUsernames() {
        List<ServerPlayerEntity> players = this.getAll();

        for (ServerPlayerEntity player : players) {
            TeamManager.hide(player);
        }
    }

    private ServerPlayerEntity draftRandom(List<ServerPlayerEntity> candidates) {
        Random random = new Random();

        ServerPlayerEntity randomPlayer = candidates.get(random.nextInt(candidates.size()));
        candidates.remove(randomPlayer);

        return randomPlayer;
    }

    public void assignSpecialRoles() {
        List<ServerPlayerEntity> candidates = this.getAll();
        Matchbox.LOGGER.info("Assigning special roles.");
        this.setRole(this.draftRandom(candidates), Role.SPARK);
        if (candidates.isEmpty()) { return; }
        this.setRole(this.draftRandom(candidates), Role.MEDIC);
    }

    public void randomizeLocations() {
        List<ServerPlayerEntity> players = world.getPlayers();

        MinecraftServer server = world.getServer();
        if (server == null) {
            Matchbox.LOGGER.error("Server is null!");
            return;
        }

        List<BlockPos> spawnLocations = DataManager.getGlobalSpawnLocations(server).getBlockPosList();
        if (spawnLocations.isEmpty()) {
            Matchbox.LOGGER.error("No spawn locations set.");
            return;
        }

        Random random = new Random();
        for (ServerPlayerEntity player : players) {
            int spawnIndex = random.nextInt(spawnLocations.size());
            BlockPos spawnPos = spawnLocations.get(spawnIndex);
            player.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), false);

            this.applyBlindAndSlowness(player, 3);
        }
    }

    private void applyBlindAndSlowness(ServerPlayerEntity player, int blindnessLevel) {
        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("blindness")).orElseThrow(), 150, 255, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("slowness")).orElseThrow(), 150, blindnessLevel, false, false, false));
    }

    @Nullable
    public Role getRole(UUID playerId) {
        return this.roleMap.get(playerId);
    }

    @Nullable
    public Role getRole(ServerPlayerEntity player) {
        return this.getRole(player.getUuid());
    }

    public List<ServerPlayerEntity> getAll(Predicate<ServerPlayerEntity> predicate) {
        List<ServerPlayerEntity> players = new ArrayList<>(this.world.getPlayers());
        players.removeIf(predicate.negate());

        return new ArrayList<>(players);
    }

    public List<ServerPlayerEntity> getAll() {
        return this.getAll(plr -> true);
    }

    public List<ServerPlayerEntity> getAllByRole(Role role) {
        List<ServerPlayerEntity> players = new ArrayList<>();

        for (UUID uuid : this.roleMap.keySet()) {
            Role playerRole = this.roleMap.get(uuid);
            if (!playerRole.equals(role)) { continue; }
            players.add((ServerPlayerEntity) this.world.getPlayerByUuid(uuid));
        }

        return players;
    }

    @Nullable
    public ServerPlayerEntity getById(UUID id) {
        return this.world.getPlayers(plr -> plr.getUuid().equals(id)).getFirst();
    }

}

package me.rowwyourboat.services;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.game.GameInstance;
import me.rowwyourboat.game.enums.Role;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameService {

    public static HashMap<RegistryKey<World>, GameInstance> gameInstances = new HashMap<>();

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

    public static GameInstance createNewGame(ServerWorld world) {
        GameInstance game = new GameInstance(world);
        gameInstances.put(game.getGameWorld().getRegistryKey(), game);
        return game;
    }

    public static void startGame(GameInstance game) {
        boolean succesfulSpread = spreadPlayers(game);
        if (!succesfulSpread) { return; }

        hideAllPlayerNames(game);
        assignSpecialRoles(game);
    }

    public static @Nullable GameInstance getGameFromWorld(ServerWorld world) {
        return gameInstances.get(world.getRegistryKey());
    }

    public static void endGame(GameInstance game) {
        game.purge();
        gameInstances.remove(game.getIdentifier());
    }

    private static boolean spreadPlayers(GameInstance game) {
        ServerWorld world = game.getGameWorld();
        List<ServerPlayerEntity> players = world.getPlayers();

        MinecraftServer server = world.getServer();
        if (server == null) {
            Matchbox.LOGGER.error("Server is null!");
            return false;
        }

        List<BlockPos> spawnLocations = DataService.getGlobalSpawnLocations(server).getBlockPosList();
        if (spawnLocations.isEmpty()) {
            Matchbox.LOGGER.error("No spawn locations set.");
            return false;
        }

        Random random = new Random();
        for (ServerPlayerEntity player : players) {
            int spawnIndex = random.nextInt(spawnLocations.size());
            BlockPos spawnPos = spawnLocations.get(spawnIndex);
            player.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), false);
            applyBlindAndSlowness(player, 3);
        }

        return true;
    }

    private static void applyBlindAndSlowness(ServerPlayerEntity player, int blindnessLevel) {
        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("blindness")).orElseThrow(), 150, 255, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("slowness")).orElseThrow(), 150, blindnessLevel, false, false, false));
    }

    private static void hideAllPlayerNames(GameInstance game) {
        List<ServerPlayerEntity> players = game.getAllPlayers();

        for (ServerPlayerEntity player : players) {
            NameVisibilityService.hide(player);
        }
    }

    private static void assignSpecialRoles(GameInstance game) {
        List<ServerPlayerEntity> candidates = game.getAllPlayers();

        game.setPlayerRole(RoleService.draftRandomPlayer(candidates), Role.SPARK);
        if (candidates.isEmpty()) { return; }
        game.setPlayerRole(RoleService.draftRandomPlayer(candidates), Role.MEDIC);
    }
}

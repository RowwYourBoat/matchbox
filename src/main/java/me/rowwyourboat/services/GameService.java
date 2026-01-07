package me.rowwyourboat.services;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.game.GameState;
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

    public static HashMap<RegistryKey<World>, GameState> gameStates = new HashMap<>();

    /*
     * Round start:
     * 1. Disable chat
     * 2. Spread players out in the map (start with blind- and slowness)
     * 3. Assign roles
     * 4. Start a 10-minute round duration timer
     */

    /*
     * End of round (meeting):
     * 1. Player marked by the spark dies (unless they're marked by the medic and are told so)
     * 2. All players get blind- and slowness
     * 3. Enable chat
     * 4. Start a 10-minute meeting duration timer (3:30 for first round)
     * 5. Disable chat when the timer hits 0
     * 6. Show voting UI
     */

    public static GameState createNewGame(ServerWorld world) {
        Matchbox.LOGGER.info(world.getRegistryKey().getValue().toString());
        GameState gameState = new GameState(world);
        gameStates.put(gameState.getGameWorld().getRegistryKey(), gameState);
        return gameState;
    }

    public static void startGame(GameState gameState) {
        spreadPlayers(gameState);
        assignRoles(gameState);
    }

    public static @Nullable GameState getGameFromWorld(ServerWorld world) {
        return gameStates.get(world.getRegistryKey());
    }

    public static void endGame(GameState gameState) {
        gameState.purge();
        gameStates.remove(gameState.getIdentifier());
    }

    private static void spreadPlayers(GameState gameState) {
        ServerWorld world = gameState.getGameWorld();
        List<ServerPlayerEntity> players = world.getPlayers();

        MinecraftServer server = world.getServer();
        if (server == null) {
            Matchbox.LOGGER.error("Server is null!");
            return;
        }

        List<BlockPos> spawnLocations = DataService.getGlobalSpawnLocations(server).getBlockPosList();
        if (spawnLocations.isEmpty()) {
            Matchbox.LOGGER.error("No spawn locations set.");
            return;
        }

        Random random = new Random();
        for (ServerPlayerEntity player : players) {
            int spawnIndex = random.nextInt(spawnLocations.size());
            BlockPos spawnPos = spawnLocations.get(spawnIndex);
            player.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), false);
            applyBlindAndSlowness(player, 3);
        }
    }

    private static void applyBlindAndSlowness(ServerPlayerEntity player, int blindnessLevel) {
        StatusEffectInstance[] statusEffects = {
                new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.of("minecraft:blindness")).orElseThrow(), 150, 255, false, false, false),
                new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.of("minecraft:slowness")).orElseThrow(), 150, blindnessLevel, false, false, false),
        };

        for (StatusEffectInstance statusEffect : statusEffects) {
            player.addStatusEffect(statusEffect);
        }
    }

    private static void assignRoles(GameState gameState) {

    }
}

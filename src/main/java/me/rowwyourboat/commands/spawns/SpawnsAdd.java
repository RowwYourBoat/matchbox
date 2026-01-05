package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.data.SpawnLocations;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * Command for adding a new spawn location at the player's current location
 */
public class SpawnsAdd {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        if (player == null) { return 0; }

        BlockPos pos = player.getBlockPos();
        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) { return 0; }

        SpawnLocations spawnLocations = SpawnLocations.getSpawnLocationsData(server);
        spawnLocations.add(pos);

        player.sendMessage(Text.of("Added spawn location at " + pos.toShortString()));
        return Command.SINGLE_SUCCESS;
    }

}

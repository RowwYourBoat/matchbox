package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.data.SpawnLocations;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Command for clearing all saved spawn locations
 */
public class SpawnsClear {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        if (player == null) { return 0; }

        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) { return 0; }

        SpawnLocations.getSpawnLocationsData(server).clear();
        player.sendMessage(Text.of("Cleared all spawn locations."));

        return Command.SINGLE_SUCCESS;
    }

}

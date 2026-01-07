package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.services.DataService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SpawnsRemove {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        if (player == null) { return 0; }

        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) { return 0; }

        Integer spawnIndex = commandContext.getArgument("index", Integer.class);
        DataService.getGlobalSpawnLocations(server).remove(spawnIndex);

        player.sendMessage(Text.of("Removed spawn location at index " + spawnIndex + "."));
        return Command.SINGLE_SUCCESS;
    }

}

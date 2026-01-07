package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.data.SpawnLocations;
import me.rowwyourboat.services.DataService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SpawnsAdd {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        if (player == null) { return 0; }

        BlockPos pos = player.getBlockPos();
        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) { return 0; }

        SpawnLocations spawnLocations = DataService.getGlobalSpawnLocations(server);
        spawnLocations.add(pos);

        player.sendMessage(Text.of("Added spawn location at " + pos.toShortString()));
        return Command.SINGLE_SUCCESS;
    }

}

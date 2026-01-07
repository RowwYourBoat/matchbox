package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.services.DataService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SpawnsGet {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        if (player == null) { return 0; }

        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) { return 0; }

        List<BlockPos> spawnLocations = DataService.getGlobalSpawnLocations(server).getBlockPosList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Spawn locations: \n");

        if (spawnLocations.isEmpty()) {
            stringBuilder.append("There's nothing here.");
            player.sendMessage(Text.of(stringBuilder.toString()));
            return Command.SINGLE_SUCCESS;
        }

        for (int i = 0; i < spawnLocations.size(); i++) {
            String line = "[" + i + "] " + spawnLocations.get(i).toShortString();
            if (i < spawnLocations.size() - 1) {
                line += ", \n";
            }
            stringBuilder.append(line);
        }

        player.sendMessage(Text.of(stringBuilder.toString()));
        return Command.SINGLE_SUCCESS;
    }

}

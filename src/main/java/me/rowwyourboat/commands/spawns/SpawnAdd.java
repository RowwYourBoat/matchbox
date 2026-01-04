package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Command for adding a new spawn location at the player's current location
 */
public class SpawnAdd {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        if (player == null) { return 0; }
        player.sendMessage(Text.of("Adding spawn location"));

        return Command.SINGLE_SUCCESS;
    }

}

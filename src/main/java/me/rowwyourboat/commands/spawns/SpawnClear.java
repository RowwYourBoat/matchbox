package me.rowwyourboat.commands.spawns;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * Command for clearing all saved spawn locations
 */
public class SpawnClear {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendMessage(Text.of("Clearing spawn locations"));
        return Command.SINGLE_SUCCESS;
    }

}

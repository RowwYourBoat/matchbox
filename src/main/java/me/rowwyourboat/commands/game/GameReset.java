package me.rowwyourboat.commands.game;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class GameReset {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendMessage(Text.of(commandContext.getInput()));
        return Command.SINGLE_SUCCESS;
    }

}

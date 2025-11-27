package me.rowwyourboat.commands.game;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.commands.abstracts.AbstractCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Start extends AbstractCommand {

    public int execute(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendMessage(Text.of(commandContext.getInput()));
        return Command.SINGLE_SUCCESS;
    }

}

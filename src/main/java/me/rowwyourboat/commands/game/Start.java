package me.rowwyourboat.commands.game;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.commands.abstracts.AbstractCommand;
import me.rowwyourboat.game.GameManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Start extends AbstractCommand {

    public int execute(CommandContext<ServerCommandSource> commandContext) {
        if (GameManager.gameInProgress) {
            commandContext.getSource().sendError(Text.of("Game is in progress!"));
            return 0;
        }

        GameManager.newGame();
        return Command.SINGLE_SUCCESS;
    }

}

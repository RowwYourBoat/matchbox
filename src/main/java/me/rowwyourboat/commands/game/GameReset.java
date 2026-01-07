package me.rowwyourboat.commands.game;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.game.GameState;
import me.rowwyourboat.services.GameService;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class GameReset {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerWorld world = commandContext.getSource().getWorld();
        GameState gameState = GameService.getGameFromWorld(world);
        if (gameState == null) {
            commandContext.getSource().sendError(Text.of("There is no game in progress!"));
            return 0;
        }

        GameService.endGame(gameState);

        commandContext.getSource().sendMessage(Text.of("Game has been reset."));
        return Command.SINGLE_SUCCESS;
    }

}

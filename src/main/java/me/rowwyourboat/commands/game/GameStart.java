package me.rowwyourboat.commands.game;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.rowwyourboat.game.GameInstance;
import me.rowwyourboat.services.GameService;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class GameStart {

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        ServerWorld world = commandContext.getSource().getWorld();
        GameInstance game = GameService.getGameFromWorld(world);
        if (game != null) {
            commandContext.getSource().sendError(Text.of("Game is in progress!"));
            return 0;
        }

        GameInstance newGame = GameService.createNewGame(world);
        try {
            GameService.startGame(newGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

        commandContext.getSource().sendMessage(Text.of("Game has been started."));
        return Command.SINGLE_SUCCESS;
    }

}

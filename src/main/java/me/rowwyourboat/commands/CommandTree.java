package me.rowwyourboat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandTree {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("matchbox")
                            .requires(src -> src.hasPermissionLevel(4))
                            .then(
                                    CommandManager.literal("game")
                                    .then(
                                            CommandManager.literal("start")
                                                .executes(CommandTree::execute)
                                    )
                                    .then(
                                            CommandManager.literal("reset")
                                                .executes(CommandTree::execute)
                                    )
                            )
            );
        });
    }

    public static int execute(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendMessage(Text.of("Matchbox!"));
        return Command.SINGLE_SUCCESS;
    };

}

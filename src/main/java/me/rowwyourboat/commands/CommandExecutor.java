package me.rowwyourboat.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.rowwyourboat.Matchbox;
import me.rowwyourboat.commands.game.GameReset;
import me.rowwyourboat.commands.game.GameStart;
import me.rowwyourboat.commands.spawns.SpawnAdd;
import me.rowwyourboat.commands.spawns.SpawnClear;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandExecutor {

    public CommandExecutor() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                    CommandManager.literal("matchbox")
                            .requires(src -> src.getPermissions().hasPermission(Matchbox.ownerPermissionLevel))
                            .then(this.registerGameSubCommands())
                            .then(this.registerSpawnSubCommands())
            )
        );
    }

    private LiteralArgumentBuilder<ServerCommandSource> registerGameSubCommands() {
        return CommandManager.literal("game")
                .then(
                        CommandManager.literal("start")
                                .executes(GameStart::execute)
                )
                .then(
                        CommandManager.literal("reset")
                                .executes(GameReset::execute)
                );
    }

    private LiteralArgumentBuilder<ServerCommandSource> registerSpawnSubCommands() {
        return CommandManager.literal("spawn")
                .then(
                        CommandManager.literal("add")
                                .executes(SpawnAdd::execute)
                )
                .then(
                        CommandManager.literal("clear")
                                .executes(SpawnClear::execute)
                );
    }

}

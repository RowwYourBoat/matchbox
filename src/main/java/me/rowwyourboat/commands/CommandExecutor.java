package me.rowwyourboat.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.rowwyourboat.Matchbox;
import me.rowwyourboat.commands.abstracts.AbstractCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CommandExecutor {

    private final HashMap<String, AbstractCommand> commands = new HashMap<>();

    public CommandExecutor() {
        this.initCommands();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(
                    CommandManager.literal("matchbox")
                            .requires(src -> src.getPermissions().hasPermission(Matchbox.ownerPermissionLevel))
                            .then(this.registerGameSubCommands())
            )
        );
    }

    private void initCommands() {
        try {
            for (SubCommands cmd : SubCommands.values()) {
                commands.put(cmd.getName(), cmd.getClazz().getConstructor().newInstance());
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private LiteralArgumentBuilder<ServerCommandSource> registerGameSubCommands() {
        return CommandManager.literal("game")
                .then(
                        CommandManager.literal(SubCommands.START.getName())
                                .executes(ctx -> this.commands.get(SubCommands.START.getName()).execute(ctx))
                )
                .then(
                        CommandManager.literal(SubCommands.RESET.getName())
                                .executes(ctx -> this.commands.get(SubCommands.RESET.getName()).execute(ctx))
                );
    }

}

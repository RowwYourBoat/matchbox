package me.rowwyourboat.commands;

import me.rowwyourboat.commands.abstracts.AbstractCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CommandExecutor {

    private final HashMap<String, AbstractCommand> commands = new HashMap<>();

    public void register() {
        this.initCommands();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("matchbox")
                            .requires(src -> src.hasPermissionLevel(4))
                            .then(
                                    CommandManager.literal("game")
                                    .then(
                                            CommandManager.literal(SubCommands.START.getName())
                                                .executes(ctx -> this.commands.get(SubCommands.START.getName()).execute(ctx))
                                    )
                                    .then(
                                            CommandManager.literal(SubCommands.RESET.getName())
                                                .executes(ctx -> this.commands.get(SubCommands.RESET.getName()).execute(ctx))
                                    )
                            )
            );
        });
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

}

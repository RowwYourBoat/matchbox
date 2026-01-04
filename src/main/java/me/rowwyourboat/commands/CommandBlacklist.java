package me.rowwyourboat.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class CommandBlacklist {

    static final String[] blacklist = { "msg", "tell", "w", "teammsg", "tm", "me", "list" };

    public static void disableAll() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RootCommandNode<ServerCommandSource> rootNode = dispatcher.getRoot();

            for (String name : blacklist) {
                CommandNode<ServerCommandSource> override = CommandManager.literal(name).executes(CommandBlacklist::executionOverride).build();
                rootNode.addChild(override);

                CommandBlacklist.recursiveOverride(rootNode.getChild(name));
            }
        });
    }

    private static void recursiveOverride(CommandNode<ServerCommandSource> parentNode) {
        Collection<CommandNode<ServerCommandSource>> childNodes = parentNode.getChildren();
        if (childNodes.isEmpty()) { return; }

        for (CommandNode<ServerCommandSource> childNode : childNodes) {
            CommandNode<ServerCommandSource> override = CommandManager.literal(childNode.getName()).executes(CommandBlacklist::executionOverride).build();
            parentNode.addChild(override);

            CommandBlacklist.recursiveOverride(childNode);
        }
    }

    private static int executionOverride(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendError(Text.of("This command is disabled."));
        return 0;
    }

}

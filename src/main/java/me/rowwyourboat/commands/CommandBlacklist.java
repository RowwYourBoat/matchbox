package me.rowwyourboat.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.Arrays;

public class CommandBlacklist {

    static final String[] blacklist = { "msg", "tell", "w", "teammsg", "tm", "me", "list" };

    /**
     * Remove all blacklisted commands from the command dispatcher.
     */
    public static void removeAll() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.getRoot().getChildren().removeIf(child ->
                    Arrays.stream(blacklist).anyMatch(str -> str.equalsIgnoreCase(child.getName()))
                )
        );
    }

}

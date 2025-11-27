package me.rowwyourboat.commands;

import me.rowwyourboat.commands.abstracts.AbstractCommand;
import me.rowwyourboat.commands.game.Reset;
import me.rowwyourboat.commands.game.Start;

public enum SubCommands {
    START("start", Start.class),
    RESET("reset", Reset.class);

    private final String subName;
    private final Class<? extends AbstractCommand> command;

    SubCommands(final String subName, Class<? extends AbstractCommand> command) {
        this.subName = subName;
        this.command = command;
    }

    public String getName() {
        return this.subName;
    }

    public Class<? extends AbstractCommand> getClazz() {
        return this.command;
    }
}

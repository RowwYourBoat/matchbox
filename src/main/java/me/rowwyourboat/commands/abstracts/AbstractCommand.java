package me.rowwyourboat.commands.abstracts;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public abstract class AbstractCommand {

    public abstract int execute(CommandContext<ServerCommandSource> commandContext);

}

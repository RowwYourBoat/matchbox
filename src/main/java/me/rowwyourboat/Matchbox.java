package me.rowwyourboat;

import me.rowwyourboat.commands.CommandExecutor;
import me.rowwyourboat.events.AfterDamageEvents;
import me.rowwyourboat.events.BlockEvents;
import me.rowwyourboat.players.NameVisibility;
import me.rowwyourboat.players.SkinOverrider;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matchbox implements ModInitializer {
	public static final String MOD_ID = "matchbox";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private CommandExecutor commandExecutor;

	@Override
	public void onInitialize() {
        new SkinOverrider();
        this.registerEvents();
        this.registerCommands();
	}

    private void registerEvents() {
        NameVisibility.registerEvents();
        AfterDamageEvents.register();
        BlockEvents.register();
    }

    private void registerCommands() {
        this.commandExecutor = new CommandExecutor();
        this.commandExecutor.register();
    }

}
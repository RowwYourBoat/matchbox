package me.rowwyourboat;

import me.rowwyourboat.commands.CommandExecutor;
import me.rowwyourboat.commands.CommandBlacklist;
import me.rowwyourboat.events.AfterDamageEvents;
import me.rowwyourboat.events.BlockEvents;
import me.rowwyourboat.events.ChatEvents;
import me.rowwyourboat.game.GameManager;
import me.rowwyourboat.players.NameVisibility;
import me.rowwyourboat.players.SkinOverrider;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matchbox implements ModInitializer {
	public static final String MOD_ID = "matchbox";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Permission.Level ownerPermissionLevel = new Permission.Level(PermissionLevel.OWNERS);

	@Override
	public void onInitialize() {
        this.registerEvents();

        new SkinOverrider();
        new CommandExecutor();

        CommandBlacklist.removeAll();
	}

    private void registerEvents() {
        GameManager.registerEvents();
        NameVisibility.registerEvents();
        AfterDamageEvents.register();
        BlockEvents.register();
        ChatEvents.register();
    }
}
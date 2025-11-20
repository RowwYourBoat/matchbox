package me.rowwyourboat;

import me.rowwyourboat.players.SkinOverrider;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matchbox implements ModInitializer {
	public static final String MOD_ID = "matchbox";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        new SkinOverrider();
	}
}
package me.rowwyourboat;

import me.rowwyourboat.keybinds.SparkAbilityKeybind;
import net.fabricmc.api.ClientModInitializer;

public class MatchboxClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SparkAbilityKeybind.register();
	}
}
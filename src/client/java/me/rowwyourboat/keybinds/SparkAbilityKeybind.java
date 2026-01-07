package me.rowwyourboat.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

public class SparkAbilityKeybind {

    public static void register() {
        KeyBindingHelper.registerKeyBinding(new KeyBinding("key.matchbox.spark_ability", 82, KeyBinding.Category.MISC));
    }

}

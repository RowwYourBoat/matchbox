package me.rowwyourboat.input;

import me.rowwyourboat.Matchbox;
import me.rowwyourboat.MatchboxClient;
import me.rowwyourboat.network.payloads.MarkPlayerC2SPayload;
import me.rowwyourboat.network.payloads.SparkAbilityC2SPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class InputActions {

    private static KeyBinding.Category matchboxBindingCategory;
    private static KeyBinding sparkAbilityBinding;
    private static int cooldown = 0;

    public static void register() {
        matchboxBindingCategory = KeyBinding.Category.create(Identifier.of(Matchbox.MOD_ID, "abilities"));
        sparkAbilityBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.matchbox.spark_ability", 82, matchboxBindingCategory));

        ClientTickEvents.END_CLIENT_TICK.register(InputActions::onEndClientTick);
    }

    private static void onEndClientTick(MinecraftClient client) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Spark ability
        if (sparkAbilityBinding.wasPressed()) {
            ClientPlayNetworking.send(SparkAbilityC2SPayload.INSTANCE);
            cooldown = 5;
            return;
        }

        // Player marking
        if (!client.mouse.wasRightButtonClicked()) { return; }
        PlayerLikeEntity target = MatchboxClient.getTargetedPlayer(client);

        if (target == null) { return; }
        ClientPlayNetworking.send(new MarkPlayerC2SPayload(Optional.of(target.getUuid())));
        cooldown = 5;
    }

}

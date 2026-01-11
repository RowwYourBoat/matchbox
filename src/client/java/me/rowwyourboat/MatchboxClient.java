package me.rowwyourboat;

import me.rowwyourboat.input.InputActions;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.jspecify.annotations.Nullable;

public class MatchboxClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		InputActions.register();
	}

	public static @Nullable OtherClientPlayerEntity getTargetedPlayer(MinecraftClient client) {
		ClientPlayerEntity player = client.player;
		if (player == null) { return null; }

		Entity cameraEntity = client.getCameraEntity();
		if (cameraEntity == null) { return null; }

		HitResult result = player.getCrosshairTarget(0f, cameraEntity);
		if (!(result instanceof EntityHitResult entityResult)) { return null; }

		Entity targetedEntity = entityResult.getEntity();
		if (!(targetedEntity instanceof OtherClientPlayerEntity targetedPlayer)) { return null; }

		return targetedPlayer;
	}

}
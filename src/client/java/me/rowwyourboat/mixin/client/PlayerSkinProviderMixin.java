package me.rowwyourboat.mixin.client;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import net.minecraft.client.texture.PlayerSkinProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;

@Mixin(PlayerSkinProvider.class)
public class PlayerSkinProviderMixin {
	@ModifyVariable(method = "fetchSkinTextures(Ljava/util/UUID;Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private MinecraftProfileTextures modifyTextures(MinecraftProfileTextures textures) {
		HashMap<String, String> metadata = new HashMap<>();
		metadata.put("model", "wide");

		MinecraftProfileTexture skinTexture = new MinecraftProfileTexture("http://textures.minecraft.net/texture/b90696ebc74ce7a900ec8abeec0dc1ccb3534c1b8ba6cbd9e83c5cd7f381fb48", metadata);
		return new MinecraftProfileTextures(skinTexture, null, null, textures.signatureState());
	}
}
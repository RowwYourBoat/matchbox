package me.rowwyourboat.players;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.rowwyourboat.Matchbox;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SkinOverrider {

    private final String steveSkinUrl = "http://textures.minecraft.net/texture/b90696ebc74ce7a900ec8abeec0dc1ccb3534c1b8ba6cbd9e83c5cd7f381fb48";

    private final Base64.Encoder encoder;

    public SkinOverrider() {
        this.encoder = Base64.getEncoder();
        this.registerServerEvents();
    }

    private void registerServerEvents() {
        ServerPlayerEvents.JOIN.register(this::overrideSkin);
    }

    private void overrideSkin(ServerPlayerEntity player) {
        GameProfile originalProfile = player.getGameProfile();

        // Create a new Multimap since the GameProfile's PropertyMap is immutable
        Multimap<String, Property> mutableMultimap = MultimapBuilder.hashKeys().arrayListValues().build();

        // Copy properties from the original GameProfile to the mutable Multimap
        for (Map.Entry<String, Property> entry : originalProfile.properties().entries()) {
            String key = entry.getKey();
            if (key == null || key.equals("textures")) { continue; }
            mutableMultimap.put(key, entry.getValue());
        }

        // Add the `textures` property to the mutable Multimap
        String encodedOverride = this.getEncodedTexturesOverride(originalProfile);
        mutableMultimap.put("textures", new Property("textures", encodedOverride));

        // Create a new GameProfile with the mutable Multimap properties
        GameProfile profileOverride = new GameProfile(originalProfile.id(), originalProfile.name(), new PropertyMap(mutableMultimap));

        try {
            // Override the GameProfile of the player using reflection since it doesn't have a setter
            Field profileField = player.getClass().getSuperclass().getDeclaredField("gameProfile");
            profileField.setAccessible(true);
            profileField.set(player, profileOverride);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Matchbox.LOGGER.error("Failed to override GameProfile for {}: {}", player.getName(), e.getMessage());
            return;
        }

        this.updatePlayerAppearance(player);
    }

    private @NotNull String getEncodedTexturesOverride(GameProfile profile) {
        long profileTimestamp = new Date().getTime();
        String profileId = profile.id().toString().replaceAll("-", "");
        String profileName = profile.name();

        String texturesOverride = String.format(
                """
                    {
                      "timestamp" : %d,
                      "profileId" : "%s",
                      "profileName" : "%s",
                      "signatureRequired" : true,
                      "textures" : {
                        "SKIN" : {
                          "url" : "%s"
                        }
                      }
                    }
                """,

                profileTimestamp,
                profileId,
                profileName,
                steveSkinUrl
            );

        return this.encoder.encodeToString(texturesOverride.getBytes(StandardCharsets.UTF_8));
    }

    private void updatePlayerAppearance(ServerPlayerEntity player) {
        for (ServerPlayerEntity otherPlayer : player.getEntityWorld().getPlayers()) {
            otherPlayer.networkHandler.sendPacket(
                new PlayerRemoveS2CPacket(
                    List.of(player.getUuid())
                )
            );
            otherPlayer.networkHandler.sendPacket(
                new PlayerListS2CPacket(
                    PlayerListS2CPacket.Action.ADD_PLAYER,
                    player
                )
            );
        }
    }

}
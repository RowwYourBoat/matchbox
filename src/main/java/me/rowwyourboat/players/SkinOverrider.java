package me.rowwyourboat.players;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class SkinOverrider {

    private final String steveSkinUrl = "http://textures.minecraft.net/texture/31f477eb1a7beee631c2ca64d06f8f68fa93a3386d04452ab27f43acdf1b60cb";

    private final Base64.Encoder encoder;

    public SkinOverrider() {
        this.encoder = Base64.getEncoder();
        this.registerServerEvents();
    }

    private void registerServerEvents() {
        ServerPlayerEvents.JOIN.register(this::overrideSkin);
    }

    private void overrideSkin(ServerPlayerEntity player) {
        GameProfile profile = player.getGameProfile();
        String encodedOverride = this.getEncodedOverride(profile);

        PropertyMap propertyMap = profile.getProperties();
        propertyMap.removeAll("textures");

        propertyMap.put("textures", new Property("textures", encodedOverride));
        this.updatePlayerAppearance(player);
    }

    private @NotNull String getEncodedOverride(GameProfile profile) {
        long profileTimestamp = new Date().getTime();
        String profileId = profile.getId().toString().replaceAll("-", "");
        String profileName = profile.getName();

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
        for (ServerPlayerEntity otherPlayer : player.getWorld().getPlayers()) {
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
package me.rowwyourboat.events;

import me.rowwyourboat.game.GameManager;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChatEvents {

    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatEvents::onAllowChatMessage);
        ServerMessageEvents.ALLOW_GAME_MESSAGE.register(ChatEvents::onAllowGameMessage);
    }

    /**
     * Only allow player chat messages when a round is not in progress.
     */
    private static boolean onAllowChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        return (!GameManager.roundInProgress);
    }

    /**
     * Only allow game messages such as advancements, death and join messages when a round is not in progress.
     */
    private static boolean onAllowGameMessage(MinecraftServer server, Text message, boolean overlay) {
        return (!GameManager.roundInProgress && !overlay);
    }

}

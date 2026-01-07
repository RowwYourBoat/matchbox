package me.rowwyourboat.events;

import me.rowwyourboat.game.GamePhase;
import me.rowwyourboat.game.GameState;
import me.rowwyourboat.services.GameService;
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

    private static boolean onAllowChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        GameState gameState = GameService.getGameFromWorld(sender.getEntityWorld());
        if (gameState == null) { return true; }

        return (gameState.getPhase() == GamePhase.IN_ROUND);
    }

    private static boolean onAllowGameMessage(MinecraftServer server, Text message, boolean overlay) {
        return false;
    }

}

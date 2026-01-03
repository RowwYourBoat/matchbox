package me.rowwyourboat.game;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

public class GameManager {

    public static boolean gameInProgress = false;
    public static boolean roundInProgress = false;
    public static long endTime = 0;


    /*
     * Round start:
     * 1. Disable chat
     * 2. Spread players out in the map (start with blind- and slowness)
     * 3. Assign roles
     * 4. Start a 10-minute round duration timer
     */

    /*
     * End of round (meeting):
     * 1. Player marked by the spark dies (unless they're marked by the medic and are told so)
     * 2. All players get blind- and slowness
     * 3. Enable chat
     * 4. Start a 10-minute meeting duration timer (3:30 for first round)
     * 5. Disable chat when the timer hits 0
     * 6. Show voting UI
     */

    public static void registerEvents() {
        ServerTickEvents.END_WORLD_TICK.register(GameManager::onEndWorldTick);
    }

    public static void onEndWorldTick(ServerWorld world) {
        if (!gameInProgress || world.getTime() < endTime) { return; }
    }

    public static void newGame() {
        gameInProgress = true;
    }
}

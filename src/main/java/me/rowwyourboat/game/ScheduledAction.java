package me.rowwyourboat.game;

import net.minecraft.server.MinecraftServer;

public class ScheduledAction {

    private final Runnable action;
    private final MinecraftServer server;

    private long tickExecutionThreshold;
    private boolean executed;

    public ScheduledAction(GameInstance game, long delayInSeconds, Runnable action) {
        this.server = game.getWorld().getServer();
        this.action = action;

        if (this.server == null) { return; }
        this.tickExecutionThreshold = this.server.getTicks() + delayInSeconds * 20;
    }

    private boolean shouldExecute() {
        return (this.server.getTicks() > this.tickExecutionThreshold) || this.hasExecuted();
    }

    public void tick() {
        if (!this.shouldExecute()) { return; }
        this.executed = true;
        this.action.run();
    }

    public boolean hasExecuted() {
        return this.executed;
    }

}

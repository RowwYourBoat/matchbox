package me.rowwyourboat.game;

import me.rowwyourboat.Matchbox;

import java.util.TimerTask;

public class CounterTask extends TimerTask {

    private final GameState gameState;

    public CounterTask(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {
        Matchbox.LOGGER.info("Time left: {}", gameState.getPhaseTimeLeft());
        gameState.decrementPhaseTimeLeft();

        if (gameState.getPhaseTimeLeft() > 0) { return; }
        gameState.onTimerEnded();
    }
}

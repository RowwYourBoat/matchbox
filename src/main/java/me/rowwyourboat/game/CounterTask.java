package me.rowwyourboat.game;

import me.rowwyourboat.Matchbox;

import java.util.TimerTask;

public class CounterTask extends TimerTask {

    private final GameInstance game;

    public CounterTask(GameInstance game) {
        this.game = game;
    }

    @Override
    public void run() {
        Matchbox.LOGGER.info("Time left: {}", game.getPhaseTimeLeft());
        game.decrementPhaseTimeLeft();

        if (game.getPhaseTimeLeft() > 0) { return; }
        game.onTimerEnded();
    }
}

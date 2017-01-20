package com.zerguy.elevenSticks.game;

import com.zerguy.elevenSticks.game.player.Player;
import com.zerguy.elevenSticks.game.player.neuralNet.Sonia;

import java.util.Random;

class Round {
    private int sticksLeft = Game.NUMBER_OF_STICKS;

    private Sonia sonia = Game.sonia;
    private Player player2;
    private boolean hasSoniaWon;

    private Random random = new Random();

    Round(final Player player2) {
        this.player2 = player2;
    }

    void play() {
        Player current = random.nextBoolean() ? sonia : player2;

        while (sticksLeft > 0) {
            int sticksToRemove = current.makeTurn(sticksLeft);

            sticksLeft -= sticksToRemove;
            current = changePlayer(current);
        }

        hasSoniaWon = (current == player2);
        sonia.updateChoices(hasSoniaWon);

        if (hasSoniaWon)
            System.out.println(sonia.name + " won");
        else
            System.out.println(player2.name + " won");
    }

    private Player changePlayer(final Player current) {
        if (current == sonia)
            return player2;

        return sonia;
    }

    public boolean hasSoniaWon() {
        return hasSoniaWon;
    }
}

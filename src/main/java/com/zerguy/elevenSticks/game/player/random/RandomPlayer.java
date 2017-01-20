package com.zerguy.elevenSticks.game.player.random;

import com.zerguy.elevenSticks.game.player.Player;

import java.util.Random;

public class RandomPlayer extends Player {

    {
        name = "Random ";
    }

    private Random random = new Random();

    @Override
    public int makeTurn(final int sticksLeft) {
        return 1 + random.nextInt(1);
    }
}

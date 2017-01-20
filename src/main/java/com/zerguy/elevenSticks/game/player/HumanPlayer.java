package com.zerguy.elevenSticks.game.player;

import java.util.Scanner;

public class HumanPlayer extends Player {

    {
        name = "Human";
    }

    private Scanner scanner = new Scanner(System.in);

    @Override
    public int makeTurn(final int sticksLeft) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
}

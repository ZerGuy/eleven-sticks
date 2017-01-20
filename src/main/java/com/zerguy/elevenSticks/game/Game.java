package com.zerguy.elevenSticks.game;

import com.zerguy.elevenSticks.forms.MainForm;
import com.zerguy.elevenSticks.game.player.HumanPlayer;
import com.zerguy.elevenSticks.game.player.Player;
import com.zerguy.elevenSticks.game.player.neuralNet.Choice;
import com.zerguy.elevenSticks.game.player.neuralNet.Sonia;
import com.zerguy.elevenSticks.game.player.random.RandomPlayer;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

public class Game implements Runnable {

    public static final String SAVES_FILE = "choices.txt";
    public static final int NUMBER_OF_STICKS = 11;

    static Sonia sonia = new Sonia();

    private MainForm form;
    private int soniaVictorySpree = 0;
    private Phase phase = Phase.One;

    public Game(MainForm form) {
        this.form = form;
    }

    private void play() {
        while (true) {
            Round round = new Round(getEnemy());
            round.play();

            updateSoniasSpree(round.hasSoniaWon());
            form.updateStats();
//            pause();
        }
    }

    private Player getEnemy() {
        switch (phase){
            case One:
                return new RandomPlayer();
            case Two:
                return new Sonia(sonia.getChoices(), "Sonia Clone");
            case Three:
                return new HumanPlayer();
        }
        throw new NoSuchElementException("There are only 3 phases");
    }

    private void updateSoniasSpree(final boolean hasSoniaWon) {
        if (hasSoniaWon)
            soniaVictorySpree++;
        else
            soniaVictorySpree = 0;

        if (soniaVictorySpree > 100) {
            phase = phase.next();
            soniaVictorySpree = 0;
        }
    }

    private void pause() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        //todo
    }


    public Map<Integer, Choice> getSoniasChoices() {
        return sonia.getChoices();
    }

    @Override
    public void run() {
        play();
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}

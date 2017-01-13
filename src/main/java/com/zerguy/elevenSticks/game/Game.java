package com.zerguy.elevenSticks.game;

import com.zerguy.elevenSticks.forms.MainForm;
import com.zerguy.elevenSticks.neuralNet.Choice;
import com.zerguy.elevenSticks.neuralNet.Sonia;

import java.util.Map;
import java.util.Random;

public class Game implements Runnable {

    public static final String SAVES_FILE = "choices.txt";
    public static final int NUMBER_OF_STICKS = 11;

    Random random = new Random();

    private MainForm form ;
    private Sonia sonia = new Sonia();

    public Game(MainForm form) {
        this.form = form;
    }

    void play() {
        while (true) {
            playRound();
            form.updateStats();
        }
    }

    private void playRound() {
        int sticksLeft = NUMBER_OF_STICKS;
        boolean isHumanTurn = random.nextBoolean();

        while (sticksLeft > 0) {
            int sticksToRemove = 0;

            if (isHumanTurn)
                sticksToRemove = random.nextInt(1) + 1;
            else
                sticksToRemove = sonia.makeTurn(sticksLeft);

            sticksLeft -= sticksToRemove;
            isHumanTurn = !isHumanTurn;
            pause();
        }

        sonia.updateChoices(isHumanTurn);

        if (!isHumanTurn)
            System.out.println("Human wins");
        else
            System.out.println("Machine wins");

    }

    private void pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        //todo
    }


    public Map<Integer, Choice> getSoniasChoices() {
        return sonia.choices;
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

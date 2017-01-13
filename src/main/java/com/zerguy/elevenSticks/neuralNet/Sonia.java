package com.zerguy.elevenSticks.neuralNet;

import com.zerguy.elevenSticks.game.Game;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;

public class Sonia {

    public Map<Integer, Choice> choices = new LinkedHashMap<>();

    private Random random = new Random();
    private Map<Integer, Integer> roundChoices = null;

    public Sonia() {
        loadChoiceMap();
    }

    private void loadChoiceMap() {
        String saves = "";
        try {
            saves = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(Game.SAVES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(saves)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");

                int sticksNumber = Integer.parseInt(tokens[0]);
                int take1 = Integer.parseInt(tokens[1]);
                int take2 = Integer.parseInt(tokens[2]);
                choices.put(sticksNumber, new Choice(take1, take2));
            }

            scanner.close();
        }
    }

    public int makeTurn(int sticksLeft) {
        if (roundChoices == null)
            roundChoices = new HashMap<>();

        Choice choice = choices.get(sticksLeft);
        int upperLimit = choice.take1 + choice.take2;

        int rand = random.nextInt(upperLimit);

        if (rand >= choice.take1)
            return take2Sticks(sticksLeft);

        return take1Stick(sticksLeft);
    }

    private int take1Stick(int sticksLeft) {
        roundChoices.put(sticksLeft, 1);
        return 1;
    }

    private int take2Sticks(int sticksLeft) {
        roundChoices.put(sticksLeft, 2);
        return 2;
    }

    public void updateChoices(boolean isVictory) {
        roundChoices.entrySet().forEach(entry -> {
            if (isVictory)
                safeStrategy(entry);
            else
                deleteStrategy(entry);
        });

        roundChoices = null;
    }

    private void deleteStrategy(Map.Entry<Integer, Integer> entry) {
        int stickTaken = entry.getValue();
        Choice choice = choices.get(entry.getKey());

        if (stickTaken == 1 && choice.take1 > 1)
            choice.take1--;

        if (stickTaken == 2 && choice.take2 > 1)
            choice.take2--;
    }

    private void safeStrategy(Map.Entry<Integer, Integer> entry) {
        int stickTaken = entry.getValue();
        Choice choice = choices.get(entry.getKey());

        if (stickTaken == 1)
            choice.take1++;
        else
            choice.take2++;
    }
}

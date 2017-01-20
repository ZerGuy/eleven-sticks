package com.zerguy.elevenSticks.game.player.neuralNet;

import com.zerguy.elevenSticks.game.Game;
import com.zerguy.elevenSticks.game.player.Player;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;

public class Sonia extends Player {

    private Map<Integer, Choice> choices = new LinkedHashMap<>();

    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 1000;

    private Random random = new Random();
    private Map<Integer, Integer> roundChoices = null;

    {
        name = "Sonia";
    }

    public Sonia() {
        loadChoiceMap();
    }

    public Sonia(final Map<Integer, Choice> choices, String name) {
        this.choices = choices;
        this.name = name;
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

        if (stickTaken == 1 && choice.take1 > LOWER_BOUND)
            choice.take1--;

        if (stickTaken == 2 && choice.take2 > LOWER_BOUND)
            choice.take2--;
    }

    private void safeStrategy(Map.Entry<Integer, Integer> entry) {
        int stickTaken = entry.getValue();
        Choice choice = choices.get(entry.getKey());

        if (stickTaken == 1 && choice.take1 < UPPER_BOUND)
            choice.take1++;

        if (stickTaken == 2 && choice.take2 < UPPER_BOUND)
            choice.take2++;
    }

    public Map<Integer, Choice> getChoices() {
        return Collections.unmodifiableMap(choices);
    }
}

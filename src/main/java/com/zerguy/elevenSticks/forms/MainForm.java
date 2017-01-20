package com.zerguy.elevenSticks.forms;

import com.zerguy.elevenSticks.game.Game;
import com.zerguy.elevenSticks.game.player.neuralNet.Choice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class MainForm extends JFrame {

    static class Row {
        JLabel sticksNumber;
        JLabel take1;
        JLabel take2;

        public Row(String sticksNumber, String take1, String take2) {
            this.sticksNumber = new JLabel(sticksNumber);
            this.take1 = new JLabel(take1);
            this.take2 = new JLabel(take2);
        }
    }

    private JPanel panel;
    private Row[] rows = new Row[Game.NUMBER_OF_STICKS];

    private Game game = new Game(this);

    public MainForm() throws HeadlessException {
        initForm();
        initGui();

        game.start();
    }

    private void initForm() {
        setTitle("11 sticks");
        setContentPane(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setLocation(150, 150);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.stop();
            }
        });
    }

    private void initGui() {
        panel.setLayout(new GridLayout(Game.NUMBER_OF_STICKS, 3));

        Map<Integer, com.zerguy.elevenSticks.game.player.neuralNet.Choice> choices = game.getSoniasChoices();

        for (int i = rows.length - 1; i >= 0; i--) {
            int sticksNumber = i + 1;
            Choice choice = choices.get(sticksNumber);


            rows[i] = new Row(sticksNumber + " sticks: ", String.valueOf(choice.take1), String.valueOf(choice.take2));

            panel.add(rows[i].sticksNumber);
            panel.add(rows[i].take1);
            panel.add(rows[i].take2);
        }

    }

    public void updateStats() {
        Map<Integer, Choice> choices = game.getSoniasChoices();

        for (int i = 0; i < rows.length; i++) {
            Choice choice = choices.get(i + 1);
            rows[i].take1.setText(String.valueOf(choice.take1));
            rows[i].take2.setText(String.valueOf(choice.take2));
        }
    }
}

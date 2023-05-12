package dev.cregg.blockbattles;

import java.text.DecimalFormat;

public class PlayerData {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public int wins;
    public int losses;


    public PlayerData(int wins, int losses) {

        this.wins = wins;
        this.losses = losses;

    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }
}

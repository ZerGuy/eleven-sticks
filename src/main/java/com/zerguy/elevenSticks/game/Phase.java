package com.zerguy.elevenSticks.game;

public enum Phase {
    One,
    Two,
    Three;

    private static Phase[] values = values();
    public Phase next() {
        return values[(this.ordinal() + 1) % values.length];
    }
}

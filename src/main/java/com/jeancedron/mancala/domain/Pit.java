package com.jeancedron.mancala.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class Pit {

    @Getter
    private final int index;

    @Getter
    private final Player player;

    @Getter
    private int stones;

    public Pit(int index, int stones, Player player) {
        this.index = index;
        this.stones = stones;
        this.player = player;
    }

    public void importStones(int stones) {
        this.stones = stones;
    }

    public void addStones(int stones) {
        this.stones += stones;
    }

    public void addStone() {
        this.addStones(1);
    }

    public void removeStones(int stones) {
        this.stones -= stones;
    }

    public void removeStone() {
        this.removeStones(1);
    }

    public void clear() {
        this.stones = 0;
    }

    public boolean hasStones() {
        return this.stones > 0;
    }

    public boolean isOwnedBy(Player player) {
        return this.player.equals(player);
    }

    public boolean isBigPit() {
        return this instanceof BigPit;
    }

}

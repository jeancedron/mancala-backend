package com.jeancedron.mancala.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    private final int startingStones;

    @Getter
    private final List<Pit> pits = new ArrayList<>();

    @Getter
    private final Player[] players;

    @Getter
    private Player winner;

    public Board(int startingStones, Player... players) {
        if (players == null || players.length < BoardDefaults.MIN_PLAYERS)
            throw new IllegalArgumentException("There must be at least two players");

        this.players = players;
        this.startingStones = startingStones;
        fillListOfPits();
    }

    private void fillListOfPits() {
        Arrays.stream(players).forEach(player -> {
            List<SmallPit> smallPits = IntStream.range(0, BoardDefaults.SMALL_PITS_PER_PLAYER)
                    .mapToObj(x -> {
                        int index = getPitIndex(player.getPosition(), x);
                        return new SmallPit(index, startingStones, player);
                    })
                    .collect(Collectors.toList());
            pits.addAll(smallPits);
            pits.add(new BigPit((BoardDefaults.PITS_PER_PLAYER * player.getPosition()) - 1, player));
        });
    }

    private int getPitIndex(int playerPosition, int pitIndex) {
        return (playerPosition - 1) * BoardDefaults.PITS_PER_PLAYER + pitIndex;
    }

    /**
     * This method move all the stones from a specific pit into the next ones.
     * It also add the stones from the pit in front if the last stone arrives
     * into an own empty pit.
     *
     * @param pit    The pit to start taking stones from
     * @param player The player that is currently executing the movement
     * @return true if the player has to play again
     */
    public boolean moveStonesFromPit(Pit pit, Player player) {
        Pit lastPit = pit;
        Pit ownBigPit = getBigPit(player);

        do lastPit = sowIntoRight(player, pit, getNextPit(lastPit));
        while (pit.hasStones());

        if (landedInEmptyPit(player, lastPit))
            addToBigPitOwnAndInFrontStones(ownBigPit, lastPit);

        lookForWinner();

        return lastPit.equals(ownBigPit) && !anyPlayerLost();
    }

    private Pit getBigPit(Player player) {
        int index = player.getPosition() * BoardDefaults.BIG_PIT_DEFAULT_INDEX - 1;
        return pits.get(index);
    }

    private Pit sowIntoRight(Player player, Pit pit, Pit currentPit) {
        if (!landedInEnemyBigPit(player, currentPit))
            sow(pit, currentPit);

        return currentPit;
    }

    private void sow(Pit pit, Pit pitToSow) {
        pitToSow.addStone();
        pit.removeStone();
    }

    private Pit getNextPit(Pit currentPit) {
        int nextIndex = currentPit.getIndex() + 1;
        if (nextIndex >= pits.size())
            nextIndex = 0;

        return pits.get(nextIndex);
    }

    private boolean landedInEnemyBigPit(Player player, Pit pit) {
        return pit.isBigPit() && !pit.isOwnedBy(player);
    }

    private boolean landedInEmptyPit(Player player, Pit pit) {
        return pit.getStones() == 1 && pit.isOwnedBy(player) && !pit.isBigPit();
    }

    private void addToBigPitOwnAndInFrontStones(Pit ownBigPit, Pit currentPit) {
        Pit pitInFront = getPitInFront(currentPit);
        if (!pitInFront.hasStones())
            return;

        int stones = currentPit.getStones() + pitInFront.getStones();

        ownBigPit.addStones(stones);
        currentPit.clear();
        pitInFront.clear();
    }

    private Pit getPitInFront(Pit pit) {
        int index = Math.abs((players.length * BoardDefaults.SMALL_PITS_PER_PLAYER) - pit.getIndex());
        return pits.get(index);
    }

    private void lookForWinner() {
        List<Move[]> playerWithMoves = Arrays.stream(players)
                .map(this::getMovesForPlayer)
                .filter(moves -> moves.length != 0)
                .collect(Collectors.toList());

        if (playerWithMoves.size() <= 1) {
            sowStonesIntoOwnBigPit();
            setWinnerToPlayerWithMaxStones();
        }

    }

    private void sowStonesIntoOwnBigPit() {
        Arrays.stream(players).forEach(player -> {
            List<Pit> boardOfPlayer = getBoardOfPlayer(player);
            sowStonesIntoOwnBigPitForPlayer(player, boardOfPlayer);
        });
    }

    private void setWinnerToPlayerWithMaxStones() {
        getPits()
                .stream()
                .filter(Pit::isBigPit)
                .max(Comparator.comparing(Pit::getStones))
                .ifPresent(biggestPit -> this.winner = biggestPit.getPlayer());
    }

    private void sowStonesIntoOwnBigPitForPlayer(Player player, List<Pit> boardOfPlayer) {
        AtomicInteger stones = new AtomicInteger();
        boardOfPlayer
                .stream()
                .filter(pit -> !pit.isBigPit())
                .forEach(pit -> {
                    stones.addAndGet(pit.getStones());
                    pit.clear();
                });

        Pit bigPit = getBigPit(player);
        bigPit.addStones(stones.get());
    }

    /**
     * @return true if there is a winner for the board
     */
    public boolean anyPlayerLost() {
        return winner != null;
    }

    /**
     * This method search for the available moves for the current state of
     * the board for a specific player.
     *
     * @param player The player to search the moves for
     * @return The moves available to play
     */
    public Move[] getMovesForPlayer(Player player) {
        return getBoardOfPlayer(player)
                .stream()
                .filter(Pit::hasStones)
                .map(item -> new Move(player, item))
                .toArray(Move[]::new);
    }

    private List<Pit> getBoardOfPlayer(Player player) {
        return pits.stream()
                .filter(pit -> pit.getPlayer().equals(player) && !pit.isBigPit())
                .collect(Collectors.toList());
    }

    /**
     * Export pits is called to export the current board to an array of ints.
     * This array exported can be used later to move the board to this state.
     *
     * @return An array of ints with the current state of the board
     */
    public int[] exportPits() {
        return pits.stream().mapToInt(Pit::getStones).toArray();
    }

    /**
     * Import pits updates the current board with the array provided.
     * The input for this method is the output from {@link #exportPits()}
     *
     * @param pits The pits array of ints to restore the board
     */
    public void importPits(int[] pits) {
        if (importedPitsAreInvalid(pits))
            throw new IllegalArgumentException("pits are invalid for this board");

        IntStream.range(0, pits.length)
                .forEach(i -> this.getPits().get(i).importStones(pits[i]));
        lookForWinner();
    }

    private boolean importedPitsAreInvalid(int[] pits) {
        return pits == null || pits.length != this.pits.size();
    }

    private static final class BoardDefaults {
        private static final int MIN_PLAYERS = 2;
        private static final int SMALL_PITS_PER_PLAYER = 6;
        private static final int PITS_PER_PLAYER = 7;
        private static final int BIG_PIT_DEFAULT_INDEX = 7;
    }

}

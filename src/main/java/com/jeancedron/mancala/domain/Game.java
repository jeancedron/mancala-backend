package com.jeancedron.mancala.domain;

import lombok.Getter;
import lombok.Value;

import java.util.Arrays;

public class Game {

    @Getter
    private final GameId id;

    private final Board board;

    @Getter
    private GameState state;

    @Getter
    private Player currentPlayer;

    @Getter
    private final int startingStones;

    @Getter
    private Player winner;

    /**
     * This method is used to create a new game.
     *
     * @param startingStones stones to put in every but the big pit available
     * @param playerA        Player A to play
     * @param playerB        Player B to play
     * @return an instance of the Game
     */
    public static Game create(Integer startingStones, Player playerA, Player playerB) {
        return new Game(null, startingStones, playerA, playerB, null, null, null);
    }

    /**
     * This method is used to restore an existing game.
     * Has the same parameter as the {@link #create} method but it request id and pits.
     *
     * @param id         identifier for the existing game
     * @param pits       array with the state of the board
     * @param nextPlayer player who is going to take the next turn
     * @param gameState  the state of the board
     * @return an instance of the Game
     */
    public static Game resume(String id,
                              Integer startingStones,
                              Player playerA,
                              Player playerB,
                              int[] pits,
                              Player nextPlayer,
                              GameState gameState) {
        return new Game(new GameId(id), startingStones, playerA, playerB, pits, nextPlayer, gameState);
    }

    private Game(GameId id,
                 Integer startingStones,
                 Player playerA,
                 Player playerB,
                 int[] pits,
                 Player nextPlayer,
                 GameState gameState) {
        this.id = id;
        this.board = new Board(startingStones, playerA, playerB);
        this.currentPlayer = playerA;
        this.startingStones = startingStones;
        this.state = GameState.STARTED;

        if (pits != null) {
            board.importPits(pits);
            updateGameIfWinner();
        }

        if (nextPlayer != null)
            setNextPlayer(nextPlayer);

        if (gameState != null)
            this.state = gameState;

    }

    private void setNextPlayer(Player nextPlayer) {
        if (Arrays.asList(board.getPlayers()).contains(nextPlayer))
            this.currentPlayer = nextPlayer;
    }

    /**
     * Executes a valid movement. This movement has to be present in the array of
     * available movements for the current player. It shouldn't be a movement in a
     * null or big pit and the game must not be {@link GameState#FINISHED}
     *
     * @param move The move to perform the action
     * @return a {@link MoveResult} with the result of the movement
     */
    public MoveResult executeMovement(Move move) {
        if (!isMoveAllowed(move))
            throw new IllegalArgumentException("Move is not allowed");

        boolean shouldPlayAgain = board.moveStonesFromPit(move.getStartingPit(), move.getPlayer());

        updateGameIfWinner();

        if (!shouldPlayAgain && !board.anyPlayerLost())
            currentPlayer = getFollowingPlayer();

        return new MoveResult(shouldPlayAgain, currentPlayer, board.getWinner());
    }

    /**
     * @param startingPit    The index of the pit to start sowing
     * @param playerPosition The position of the player that is executing the movement
     * @return executes method {@link #executeMovement(Move)}
     */
    public MoveResult executeMovement(int startingPit, int playerPosition) {
        Move move = getMove(startingPit, playerPosition);
        if (move == null)
            throw new IllegalArgumentException("Move doesn't exist for player");

        return executeMovement(move);
    }

    private boolean isMoveAllowed(Move move) {
        return isMoveAvailable(move.getStartingPit().getIndex(), move.getPlayer().getPosition())
                || state == GameState.FINISHED
                || move.getStartingPit() == null
                || move.getStartingPit().isBigPit();
    }

    private boolean isMoveAvailable(int startingPit, int playerPosition) {
        return Arrays
                .stream(getAvailableMoves())
                .anyMatch(move -> move.getPlayer().getPosition() == playerPosition &&
                        move.getStartingPit().getIndex() == startingPit);
    }

    private Move getMove(int startingPit, int playerPosition) {
        return Arrays.stream(getAvailableMoves())
                .filter(move -> move.getPlayer().getPosition() == playerPosition &&
                        move.getStartingPit().getIndex() == startingPit)
                .findFirst()
                .orElse(null);
    }

    private void updateGameIfWinner() {
        if (board.anyPlayerLost()) {
            finishGame();
            this.winner = board.getWinner();
        }
    }

    private void finishGame() {
        state = GameState.FINISHED;
        currentPlayer = null;
    }

    private Player getFollowingPlayer() {
        Player[] players = board.getPlayers();
        int index = currentPlayer.getPosition() % players.length;
        return players[index];
    }

    /**
     * Method obtain the available moves for the current player
     * before executing a new movement.
     *
     * @return An array of {@link Move} available
     */
    public Move[] getAvailableMoves() {
        return board.getMovesForPlayer(currentPlayer);
    }

    /**
     * @return An array of {@link Player} playing the game
     */
    public Player[] getPlayers() {
        return board.getPlayers();
    }

    /**
     * @return An array of int containing the current board for the game
     */
    public int[] getPits() {
        return board.exportPits();
    }

    @Value
    public static class GameId {
        String value;
    }

    public enum GameState {
        STARTED,
        FINISHED
    }

}
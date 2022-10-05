package com.jeancedron.mancala.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    int startingStones = 6;
    Player playerA = new Player("Jean", 1);
    Player playerB = new Player("some-random-company.com", 2);

    @Test
    void createNewGame_whenStaticCreateIsCalled() {
        Game game = Game.create(startingStones, playerA, playerB);
        assertNotNull(game);
        assertEquals(startingStones, game.getStartingStones());
    }

    @Test
    void returnPlayers_whenRequested() {
        int expectedPlayers = 2;
        Game game = Game.create(startingStones, playerA, playerB);

        Player[] players = game.getPlayers();
        assertNotNull(players);
        assertEquals(expectedPlayers, players.length);
    }

    @Test
    void getPitsFromboard_whenRequested() {
        int[] expectedPits = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
        Game game = Game.create(startingStones, playerA, playerB);

        int[] pits = game.getPits();
        assertNotNull(pits);
        assertArrayEquals(expectedPits, pits);
    }

    @Test
    void resumeExistingGame_whenStaticResumeIsCalled() {
        String id = "anyKey";
        int[] pits = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};

        Game game = Game.resume(id, startingStones, playerA, playerB, pits, playerA, Game.GameState.STARTED);
        assertNotNull(game);
        assertEquals(id, game.getId().getValue());
    }

    @Test
    void throwException_whenMovementIsNotAllowed() {
        Game game = Game.create(startingStones, playerA, playerB);

        Pit pit = new SmallPit(1, startingStones, playerA);
        Move anyRandomMove = new Move(playerB, pit);
        assertThrows(IllegalArgumentException.class, () -> game.executeMovement(anyRandomMove));
    }

    @Test
    void returnMoveResult_whenMovementIsExecuted() {
        Game game = Game.create(startingStones, playerA, playerB);

        Move[] availableMoves = game.getAvailableMoves();
        MoveResult moveResult = game.executeMovement(availableMoves[0]);
        assertNotNull(moveResult);
    }

    @Test
    void returnMoveResult_whenMovementWithIndexIsExecuted() {
        Game game = Game.create(startingStones, playerA, playerB);

        Move[] availableMoves = game.getAvailableMoves();
        Move move = availableMoves[0];

        MoveResult moveResult = game.executeMovement(move.getStartingPit().getIndex(), move.getPlayer().getPosition());
        assertNotNull(moveResult);
    }

    @Test
    void throwIllegalArgumentException_whenMovementWithIndexIsExecutedWithInvalidValues() {
        Game game = Game.create(startingStones, playerA, playerB);
        int randomIndex = 99;

        assertThrows(IllegalArgumentException.class,
                () -> game.executeMovement(randomIndex, playerA.getPosition()));
    }

    @Test
    void finishGame_whenAnyPlayerLost() {
        String id = "anyKey";
        int[] pits = new int[]{0, 0, 0, 0, 0, 2, 0, 6, 6, 6, 6, 6, 6, 36};

        Game game = Game.resume(id, startingStones, playerA, playerB, pits, playerA, Game.GameState.STARTED);
        Move[] availableMoves = game.getAvailableMoves();

        MoveResult moveResult = game.executeMovement(availableMoves[0]);
        assertEquals(game.getState(), Game.GameState.FINISHED);
        assertNotNull(game.getWinner());
        assertEquals(moveResult.getWinner(), playerB);
    }

    @Test
    void shouldPlayAgain_whenLandsInBiggerPit() {
        String id = "anyKey";
        int[] pits = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};

        Game game = Game.resume(id, startingStones, playerA, playerB, pits, playerA, Game.GameState.STARTED);
        Move[] availableMoves = game.getAvailableMoves();
        Move move = availableMoves[0];

        MoveResult moveResult = game.executeMovement(move);
        assertTrue(moveResult.isShouldPlayAgain());
    }

    @Test
    void setNextPlayer_whenShouldntPlayAgainAndNoWinnerYet() {
        Game game = Game.create(startingStones, playerA, playerB);
        Move[] availableMoves = game.getAvailableMoves();
        Move move = availableMoves[1];

        MoveResult moveResult = game.executeMovement(move);
        assertEquals(moveResult.getNextPlayer(), playerB);
        assertNotEquals(game.getCurrentPlayer(), playerA);
    }

}

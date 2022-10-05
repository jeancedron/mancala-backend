package com.jeancedron.mancala.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    int startingStones = 6;
    Player[] twoPlayers = new Player[]{
            new Player("Jean", 1),
            new Player("some-random-company.com", 2)
    };

    @Test
    void exceptionIsThrown_whenPlayersAreLessThanTwo() {
        Player[] players = new Player[]{new Player("Jean", 1)};

        assertThrows(IllegalArgumentException.class, () -> new Board(startingStones, players));
    }

    @Test
    void exceptionIsThrown_whenPlayersIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Board(startingStones, (Player[]) null));
    }

    @Test
    void returnPlayers_whenRequested() {
        int expectedPlayers = 2;
        Board board = new Board(startingStones, twoPlayers);

        Player[] players = board.getPlayers();
        assertNotNull(players);
        assertEquals(expectedPlayers, players.length);
    }

    @Test
    void listOfPitsIsFilled_whenBoardIsCreated() {
        int expectedBoardSize = 14;

        Board board = new Board(startingStones, twoPlayers);
        assertEquals(expectedBoardSize, board.getPits().size());
    }

    @Test
    void pitsAreExported_whenRequested() {
        int[] expectedPits = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};

        Board board = new Board(startingStones, twoPlayers);
        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void pitsAreNotImported_WhenInvalidPitsAreSent() {
        int[] pits = new int[]{6, 6, 6, 6, 6, 6, 0};

        Board board = new Board(startingStones, twoPlayers);
        assertThrows(IllegalArgumentException.class, () -> board.importPits(pits));
    }

    @Test
    void pitsAreNotImported_WhenNullPitsAreSent() {
        Board board = new Board(startingStones, twoPlayers);
        assertThrows(IllegalArgumentException.class, () -> board.importPits(null));
    }

    @Test
    void pitsAreImported_whenRequested() {
        int[] pits = new int[]{0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);

        assertArrayEquals(pits, board.exportPits());
    }

    @Test
    void returnAvailableMoves_whenRequested() {
        int[] pits = new int[]{0, 2, 0, 0, 0, 0, 34, 6, 6, 6, 6, 6, 6, 0};
        int expectedMovesSize = 1;
        int expectedPitIndex = 1;

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);

        Move[] movesForPlayer = board.getMovesForPlayer(twoPlayers[0]);
        assertEquals(expectedMovesSize, movesForPlayer.length);
        assertEquals(expectedPitIndex, movesForPlayer[0].getStartingPit().getIndex());
    }

    @Test
    void returnEmptyArray_whenNoMovesAvailable() {
        int[] pits = new int[]{0, 0, 0, 0, 0, 0, 36, 6, 6, 6, 6, 6, 6, 0};
        int expectedMovesSize = 0;

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);

        Move[] movesForPlayer = board.getMovesForPlayer(twoPlayers[0]);
        assertEquals(expectedMovesSize, movesForPlayer.length);
    }

    @Test
    void sowIntoRight_whenRequested() {
        int[] expectedPits = new int[]{6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0};

        Board board = new Board(startingStones, twoPlayers);
        Pit pit = board.getPits().get(2);

        board.moveStonesFromPit(pit, twoPlayers[0]);
        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void sowIntoNext_whenLandsInEnemyBigPit() {
        int[] pits = new int[]{13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
        int[] expectedPits = new int[]{0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);

        Pit pit = board.getPits().get(0);
        board.moveStonesFromPit(pit, twoPlayers[0]);

        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void sowStone_whenLandingIntoBigPit() {
        int[] expectedPits = new int[]{0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0};

        Board board = new Board(startingStones, twoPlayers);

        Pit pit = board.getPits().get(0);
        board.moveStonesFromPit(pit, twoPlayers[0]);

        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void sowStone_whenLandingIntoBigPitAndGameFinish() {
        int[] pits = new int[]{0, 0, 0, 0, 0, 1, 1, 6, 6, 6, 6, 6, 6, 0};
        int[] expectedPits = new int[]{0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 36};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);

        Pit pit = board.getPits().get(5);
        board.moveStonesFromPit(pit, twoPlayers[0]);

        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void sowIntoRight_whenRequestedAndHasToMakeArrayCircular() {
        int[] expectedPits = new int[]{7, 7, 7, 7, 7, 0, 1, 7, 7, 7, 7, 7, 0, 1};

        Board board = new Board(startingStones, twoPlayers);
        board.moveStonesFromPit(board.getPits().get(5), twoPlayers[0]);
        board.moveStonesFromPit(board.getPits().get(12), twoPlayers[1]);

        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void stealStonesFromInFrontPit_whenLandingInEmptyPit() {
        int[] pits = new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0, 0};
        int[] expectedPits = new int[]{0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);
        Pit pit = board.getPits().get(0);

        board.moveStonesFromPit(pit, twoPlayers[0]);
        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void avoidStealingStonesFromInFrontPit_whenLandingInEmptyPitAndPitInFrontIsEmpty() {
        int[] pits = new int[]{1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0};
        int[] expectedPits = new int[]{0, 1, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);
        Pit pit = board.getPits().get(0);

        board.moveStonesFromPit(pit, twoPlayers[0]);
        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void avoidStealStonesFromInFrontPit_whenLandingInEmptyEnemyPit() {
        int[] pits = new int[]{3, 6, 0, 0, 7, 7, 1, 0, 7, 6, 6, 0, 6, 0};
        int[] expectedPits = new int[]{3, 0, 1, 1, 8, 8, 2, 1, 7, 6, 6, 0, 6, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);
        Pit pit = board.getPits().get(1);

        board.moveStonesFromPit(pit, twoPlayers[0]);
        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void avoidStealStonesFromInFrontPit_whenLandingInEmptyEnemyBigPit() {
        int[] pits = new int[]{3, 6, 0, 0, 7, 8, 1, 0, 7, 6, 6, 0, 6, 0};
        int[] expectedPits = new int[]{4, 6, 0, 0, 7, 0, 2, 1, 8, 7, 7, 1, 7, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);
        Pit pit = board.getPits().get(5);

        board.moveStonesFromPit(pit, twoPlayers[0]);
        assertArrayEquals(expectedPits, board.exportPits());
    }

    @Test
    void whenOnePlayerHasEmptyPits_thenTheOtherWins() {
        int[] pits = new int[]{0, 0, 0, 0, 0, 3, 1, 7, 7, 6, 6, 6, 6, 0};

        Board board = new Board(startingStones, twoPlayers);
        board.importPits(pits);
        Pit pit = board.getPits().get(5);
        board.moveStonesFromPit(pit, twoPlayers[0]);

        assertTrue(board.anyPlayerLost());
        assertEquals(board.getWinner(), twoPlayers[1]);
    }

}

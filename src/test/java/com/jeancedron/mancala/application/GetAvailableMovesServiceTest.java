package com.jeancedron.mancala.application;

import com.jeancedron.mancala.application.service.GetAvailableMovesService;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Move;
import com.jeancedron.mancala.domain.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetAvailableMovesServiceTest {

    private final GetAvailableMovesService getAvailableMovesService =
            new GetAvailableMovesService();

    @Test
    void getAvailableMoves_whenGameIsProvided() {
        int startingStones = 6;
        Player playerA = new Player("Jean", 1);
        Player playerB = new Player("some-random-company.com", 2);
        Game game = Game.create(startingStones, playerA, playerB);

        Move[] availableMoves = getAvailableMovesService.getAvailableMoves(game);
        assertNotNull(availableMoves);
    }


}

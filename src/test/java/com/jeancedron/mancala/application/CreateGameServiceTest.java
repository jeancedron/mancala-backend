package com.jeancedron.mancala.application;

import com.jeancedron.mancala.application.port.in.CreateGameCommand;
import com.jeancedron.mancala.application.port.out.UpdateGameStatePort;
import com.jeancedron.mancala.application.service.CreateGameService;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreateGameServiceTest {

    private final UpdateGameStatePort updateGameStatePort =
            Mockito.mock(UpdateGameStatePort.class);

    private final CreateGameService createGameService =
            new CreateGameService(updateGameStatePort);

    @Test
    void createGame_whenCommandValidIsSent() {
        int startingStones = 6;
        Player playerA = new Player("Jean", 1);
        Player playerB = new Player("some-random-company.com", 2);

        CreateGameCommand createGameCommand =
                new CreateGameCommand(startingStones, playerA.getName(), playerB.getName());
        Game game = Game.create(startingStones, playerA, playerB);
        when(updateGameStatePort.updateGame(any())).thenReturn(game);

        Game createdGame = createGameService.createGame(createGameCommand);
        assertNotNull(createdGame);
        assertEquals(createdGame.getStartingStones(), game.getStartingStones());
    }

}

package com.jeancedron.mancala.application;

import com.jeancedron.mancala.adapter.out.persistence.mongodb.DocumentNotFoundException;
import com.jeancedron.mancala.application.port.out.GetGamePort;
import com.jeancedron.mancala.application.service.GetGameService;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetGameServiceTest {

    private final GetGamePort getGamePort =
            Mockito.mock(GetGamePort.class);

    private final GetGameService getGameService =
            new GetGameService(getGamePort);

    @Test
    void getGame_whenQueryValidIsSent() {
        String id = "validId";
        int startingStones = 6;
        Player playerA = new Player("Jean", 1);
        Player playerB = new Player("some-random-company.com", 2);

        Game game = Game.create(startingStones, playerA, playerB);
        when(getGamePort.getGame(any())).thenReturn(game);

        Game obtainedGame = getGameService.getGame(id);
        assertNotNull(obtainedGame);
    }

    @Test
    void getNull_whenQueryInvalidIsSent() {
        String id = "invalidId";
        when(getGamePort.getGame(any())).thenThrow(DocumentNotFoundException.class);

        Game obtainedGame = getGameService.getGame(id);
        assertNull(obtainedGame);
    }

}

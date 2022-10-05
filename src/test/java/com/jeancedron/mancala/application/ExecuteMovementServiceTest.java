package com.jeancedron.mancala.application;

import com.jeancedron.mancala.application.port.in.ExecuteMovementCommand;
import com.jeancedron.mancala.application.port.out.UpdateGameStatePort;
import com.jeancedron.mancala.application.service.ExecuteMovementService;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.MoveResult;
import com.jeancedron.mancala.domain.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExecuteMovementServiceTest {

    UpdateGameStatePort updateGameStatePort =
            Mockito.mock(UpdateGameStatePort.class);

    private final ExecuteMovementService executeMovementService =
            new ExecuteMovementService(updateGameStatePort);

    int startingStones = 6;
    Player playerA = new Player("Jean", 1);
    Player playerB = new Player("some-random-company.com", 2);

    @Test
    void executeMovement_whenValidCommandIsProvided() {
        int startingPit = 1;

        Game game = Game.create(startingStones, playerA, playerB);
        ExecuteMovementCommand executeMovementCommand = new ExecuteMovementCommand(startingPit, game);
        when(updateGameStatePort.updateGame(any())).thenReturn(game);

        MoveResult moveResult = executeMovementService.executeMovement(executeMovementCommand);
        assertNotNull(moveResult);
    }

    @Test
    void throwException_whenInvalidCommandIsProvided() {
        int startingPit = 99;

        Game game = Game.create(startingStones, playerA, playerB);
        ExecuteMovementCommand executeMovementCommand = new ExecuteMovementCommand(startingPit, game);
        when(updateGameStatePort.updateGame(any())).thenThrow(IllegalArgumentException.class);

        MoveResult moveResult = executeMovementService.executeMovement(executeMovementCommand);
        assertNull(moveResult);
    }

}

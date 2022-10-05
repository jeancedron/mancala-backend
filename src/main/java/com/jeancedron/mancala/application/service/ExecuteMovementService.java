package com.jeancedron.mancala.application.service;

import com.jeancedron.mancala.application.port.in.ExecuteMovementCommand;
import com.jeancedron.mancala.application.port.in.ExecuteMovementUseCase;
import com.jeancedron.mancala.application.port.out.UpdateGameStatePort;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.MoveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecuteMovementService implements ExecuteMovementUseCase {

    private final UpdateGameStatePort updateGameStatePort;

    @Override
    public MoveResult executeMovement(ExecuteMovementCommand executeMovementCommand) {
        Game game = executeMovementCommand.getGame();
        try {
            MoveResult moveResult = game.executeMovement(
                    executeMovementCommand.getStartingPit(),
                    game.getCurrentPlayer().getPosition());
            updateGameStatePort.updateGame(game);
            return moveResult;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}

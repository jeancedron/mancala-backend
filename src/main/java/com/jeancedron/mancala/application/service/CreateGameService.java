package com.jeancedron.mancala.application.service;

import com.jeancedron.mancala.application.port.in.CreateGameCommand;
import com.jeancedron.mancala.application.port.in.CreateGameUseCase;
import com.jeancedron.mancala.application.port.out.UpdateGameStatePort;
import com.jeancedron.mancala.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateGameService implements CreateGameUseCase {

    private final UpdateGameStatePort updateGameStatePort;

    @Override
    public Game createGame(CreateGameCommand createGameCommand) {
        Game game = Game.create(createGameCommand.getStartingStones(),
                createGameCommand.getPlayerA(),
                createGameCommand.getPlayerB());

        return updateGameStatePort.updateGame(game);
    }

}

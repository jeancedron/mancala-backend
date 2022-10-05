package com.jeancedron.mancala.application.port.out;

import com.jeancedron.mancala.domain.Game;

public interface UpdateGameStatePort {

    Game updateGame(Game game);

}

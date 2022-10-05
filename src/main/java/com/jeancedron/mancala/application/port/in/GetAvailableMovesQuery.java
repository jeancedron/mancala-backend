package com.jeancedron.mancala.application.port.in;

import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Move;

public interface GetAvailableMovesQuery {

    Move[] getAvailableMoves(Game game);

}

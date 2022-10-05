package com.jeancedron.mancala.application.port.in;

import com.jeancedron.mancala.domain.Game;

public interface GetGameQuery {

    Game getGame(String id);

}

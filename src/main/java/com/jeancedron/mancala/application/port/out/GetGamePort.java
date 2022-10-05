package com.jeancedron.mancala.application.port.out;

import com.jeancedron.mancala.domain.Game;

public interface GetGamePort {

    Game getGame(String id);

}

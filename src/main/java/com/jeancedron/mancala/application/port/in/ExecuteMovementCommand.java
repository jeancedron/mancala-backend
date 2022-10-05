package com.jeancedron.mancala.application.port.in;

import com.jeancedron.mancala.domain.Game;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExecuteMovementCommand {

    private final int startingPit;

    private final Game game;

}

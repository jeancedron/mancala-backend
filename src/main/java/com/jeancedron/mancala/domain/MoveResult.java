package com.jeancedron.mancala.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MoveResult {

    private final boolean shouldPlayAgain;

    private final Player nextPlayer;

    private final Player winner;

}

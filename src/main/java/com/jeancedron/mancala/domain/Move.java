package com.jeancedron.mancala.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Move {

    @Getter
    private final Player player;

    @Getter
    private final Pit startingPit;

}

package com.jeancedron.mancala.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter()
@EqualsAndHashCode
@RequiredArgsConstructor
public class Player {

    private final String name;

    private final int position;

}

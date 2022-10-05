package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateGameRequest {

    @NotNull(message = "player_a must not be null")
    @JsonProperty("player_a")
    private final String playerA;

    @NotNull(message = "player_b must not be null")
    @JsonProperty("player_b")
    private final String playerB;

}

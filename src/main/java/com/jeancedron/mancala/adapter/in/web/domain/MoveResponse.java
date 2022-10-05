package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class MoveResponse {

    @JsonProperty("moves")
    private final List<MoveItemResponse> moves;

    @JsonProperty("player")
    private final PlayerResponse player;

}

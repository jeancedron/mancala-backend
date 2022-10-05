package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeancedron.mancala.domain.Move;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MoveItemResponse {

    @JsonProperty("pit")
    private final int pit;

    @JsonProperty("stones_available")
    private final int stonesAvailable;

    public static MoveItemResponse of(Move move) {
        return new MoveItemResponse(
                move.getStartingPit().getIndex(),
                move.getStartingPit().getStones()
        );
    }

}

package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeancedron.mancala.domain.MoveResult;
import com.jeancedron.mancala.domain.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MoveResultResponse {

    @JsonProperty("play_again")
    private final boolean playAgain;

    @JsonProperty("next_player")
    private final Player nextPlayer;

    @JsonProperty("winner")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Player winner;

    public static MoveResultResponse of(MoveResult moveResult) {
        return new MoveResultResponse(
                moveResult.isShouldPlayAgain(),
                moveResult.getNextPlayer(),
                moveResult.getWinner()
        );
    }

}

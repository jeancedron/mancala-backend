package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeancedron.mancala.domain.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerResponse {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("position")
    private final int position;

    public static PlayerResponse of(Player player) {
        if (player == null)
            return null;

        return new PlayerResponse(
                player.getName(),
                player.getPosition()
        );
    }

}

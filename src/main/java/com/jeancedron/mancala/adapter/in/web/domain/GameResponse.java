package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@RequiredArgsConstructor
public class GameResponse {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("players")
    private final List<PlayerResponse> players;

    @JsonProperty("state")
    private final String state;

    @JsonProperty("winner")
    private final Player winner;

    @JsonProperty("pits")
    private final int[] pits;

    @JsonProperty("next_player")
    private final PlayerResponse nextPlayer;

    public static GameResponse of(Game game) {
        String gameId = game.getId().getValue();
        List<PlayerResponse> gamePlayers = Arrays.stream(game.getPlayers())
                .map(player -> new PlayerResponse(player.getName(), player.getPosition()))
                .collect(Collectors.toList());
        PlayerResponse nextPlayer = PlayerResponse.of(game.getCurrentPlayer());
        return new GameResponse(
                gameId,
                gamePlayers,
                game.getState().toString(),
                game.getWinner(),
                game.getPits(),
                nextPlayer);
    }

}

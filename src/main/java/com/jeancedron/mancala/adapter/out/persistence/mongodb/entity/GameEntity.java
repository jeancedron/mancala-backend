package com.jeancedron.mancala.adapter.out.persistence.mongodb.entity;

import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Player;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "games")
@RequiredArgsConstructor
public class GameEntity {

    @Id
    private String id;

    private final Game.GameState state;

    private final Player nextPlayer;

    private final Player playerA;

    private final Player playerB;

    private final Player winner;

    private final int[] pits;

    private final int startingStones;

}

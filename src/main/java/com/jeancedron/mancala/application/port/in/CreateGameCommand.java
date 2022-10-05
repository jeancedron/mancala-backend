package com.jeancedron.mancala.application.port.in;

import com.jeancedron.mancala.domain.Player;
import lombok.Getter;

@Getter
public class CreateGameCommand {

    private final int startingStones;

    private final Player playerA;

    private final Player playerB;

    public CreateGameCommand(int startingStones, String playerA, String playerB) {
        this.startingStones = startingStones;
        this.playerA = new Player(playerA, 1);
        this.playerB = new Player(playerB, 2);
    }
}

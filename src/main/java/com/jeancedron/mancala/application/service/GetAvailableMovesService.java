package com.jeancedron.mancala.application.service;

import com.jeancedron.mancala.application.port.in.GetAvailableMovesQuery;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Move;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAvailableMovesService implements GetAvailableMovesQuery {

    @Override
    public Move[] getAvailableMoves(Game game) {
        return game.getAvailableMoves();
    }

}

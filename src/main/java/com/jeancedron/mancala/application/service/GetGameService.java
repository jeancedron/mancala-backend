package com.jeancedron.mancala.application.service;

import com.jeancedron.mancala.adapter.out.persistence.mongodb.DocumentNotFoundException;
import com.jeancedron.mancala.application.port.in.GetGameQuery;
import com.jeancedron.mancala.application.port.out.GetGamePort;
import com.jeancedron.mancala.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetGameService implements GetGameQuery {

    private final GetGamePort getGamePort;

    @Override
    public Game getGame(String id) {
        try {
            return getGamePort.getGame(id);
        } catch (DocumentNotFoundException ex) {
            return null;
        }
    }

}

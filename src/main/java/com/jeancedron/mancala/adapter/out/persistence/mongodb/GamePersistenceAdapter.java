package com.jeancedron.mancala.adapter.out.persistence.mongodb;

import com.jeancedron.mancala.adapter.out.persistence.mongodb.entity.GameEntity;
import com.jeancedron.mancala.adapter.out.persistence.mongodb.mapper.GameMapper;
import com.jeancedron.mancala.adapter.out.persistence.mongodb.repository.GameRepository;
import com.jeancedron.mancala.application.port.out.GetGamePort;
import com.jeancedron.mancala.application.port.out.UpdateGameStatePort;
import com.jeancedron.mancala.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GamePersistenceAdapter implements UpdateGameStatePort, GetGamePort {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public Game updateGame(Game game) {
        GameEntity gameEntity = gameMapper.mapToEntity(game);
        gameEntity = gameRepository.save(gameEntity);

        return gameMapper.mapFromEntity(gameEntity);
    }

    @Override
    public Game getGame(String id) {
        GameEntity gameEntity = gameRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);
        return gameMapper.mapFromEntity(gameEntity);
    }

}

package com.jeancedron.mancala.adapter.out.persistence.mongodb.mapper;

import com.jeancedron.mancala.adapter.out.persistence.mongodb.entity.GameEntity;
import com.jeancedron.mancala.domain.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameEntity mapToEntity(Game game) {
        GameEntity gameEntity = new GameEntity(
                game.getState(),
                game.getCurrentPlayer(),
                game.getPlayers()[0],
                game.getPlayers()[1],
                game.getWinner(),
                game.getPits(),
                game.getStartingStones()
        );

        if (game.getId() != null)
            gameEntity.setId(game.getId().getValue());

        return gameEntity;
    }

    public Game mapFromEntity(GameEntity gameEntity) {
        return Game.resume(
                gameEntity.getId(),
                gameEntity.getStartingStones(),
                gameEntity.getPlayerA(),
                gameEntity.getPlayerB(),
                gameEntity.getPits(),
                gameEntity.getNextPlayer(),
                gameEntity.getState()
        );
    }

}

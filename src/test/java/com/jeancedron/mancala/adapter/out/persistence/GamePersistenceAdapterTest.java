package com.jeancedron.mancala.adapter.out.persistence;

import com.jeancedron.mancala.adapter.out.persistence.mongodb.DocumentNotFoundException;
import com.jeancedron.mancala.adapter.out.persistence.mongodb.GamePersistenceAdapter;
import com.jeancedron.mancala.adapter.out.persistence.mongodb.entity.GameEntity;
import com.jeancedron.mancala.adapter.out.persistence.mongodb.mapper.GameMapper;
import com.jeancedron.mancala.adapter.out.persistence.mongodb.repository.GameRepository;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import({GamePersistenceAdapter.class, GameMapper.class})
public class GamePersistenceAdapterTest {

    @Autowired
    private GamePersistenceAdapter gamePersistenceAdapter;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    int startingStones = 6;
    Player playerA = new Player("Jean", 1);
    Player playerB = new Player("some-random-company.com", 2);
    int[] pits = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};

    @Test
    void getGame_whenValidIdIsSent() {
        String id = "anyId";
        Game game = Game.resume(id, startingStones, playerA, playerB, pits, playerA, Game.GameState.STARTED);
        GameEntity gameEntity = gameRepository.save(gameMapper.mapToEntity(game));

        String expectedId = gameEntity.getId();

        Game obtainedGame = gamePersistenceAdapter.getGame(gameEntity.getId());
        assertEquals(expectedId, obtainedGame.getId().getValue());
    }

    @Test
    void throwException_whenInvalidIdIsSent() {
        String id = "anyNotStoredId";

        assertThrows(DocumentNotFoundException.class, () -> gamePersistenceAdapter.getGame(id));
    }

    @Test
    void updateGame_whenValidGameIsSent() {
        Game game = Game.create(startingStones, playerA, playerB);

        Game storedGame = gamePersistenceAdapter.updateGame(game);
        assertNotNull(storedGame);
    }

}

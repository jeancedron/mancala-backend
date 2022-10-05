package com.jeancedron.mancala.adapter.out.persistence.mongodb.repository;

import com.jeancedron.mancala.adapter.out.persistence.mongodb.entity.GameEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<GameEntity, String> {

}

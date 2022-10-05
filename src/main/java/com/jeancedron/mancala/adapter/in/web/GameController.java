package com.jeancedron.mancala.adapter.in.web;

import com.jeancedron.mancala.adapter.in.web.domain.*;
import com.jeancedron.mancala.adapter.in.web.properties.GameConfigurationProperties;
import com.jeancedron.mancala.application.port.in.*;
import com.jeancedron.mancala.domain.Game;
import com.jeancedron.mancala.domain.Move;
import com.jeancedron.mancala.domain.MoveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("games")
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final ExecuteMovementUseCase executeMovementUseCase;
    private final GetGameQuery getGameQuery;
    private final GetAvailableMovesQuery getAvailableMovesQuery;

    private final GameConfigurationProperties gameProperties;

    @PostMapping(value = "", consumes = "application/json")
    ResponseEntity<GameResponse> createGame(
            @Valid @RequestBody CreateGameRequest createGameRequest
    ) {
        CreateGameCommand createGameCommand = new CreateGameCommand(
                gameProperties.getStartingStones(),
                createGameRequest.getPlayerA(),
                createGameRequest.getPlayerB());

        Game game = createGameUseCase.createGame(createGameCommand);
        return ResponseEntity.ok(GameResponse.of(game));
    }

    @GetMapping(value = "{id}")
    ResponseEntity<GameResponse> getGame(
            @PathVariable("id") String gameId
    ) {
        Game game = getGameQuery.getGame(gameId);
        if (game == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(GameResponse.of(game));
    }

    @GetMapping(value = "{id}/moves")
    ResponseEntity<MoveResponse> getGameAvailableMoves(
            @PathVariable("id") String gameId
    ) {
        Game game = getGameQuery.getGame(gameId);
        if (game == null)
            return ResponseEntity.notFound().build();

        Move[] availableMoves = getAvailableMovesQuery.getAvailableMoves(game);
        List<MoveItemResponse> moves = Arrays
                .stream(availableMoves)
                .map(MoveItemResponse::of)
                .collect(Collectors.toList());
        MoveResponse response = new MoveResponse(moves, PlayerResponse.of(game.getCurrentPlayer()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "{id}/moves", consumes = "application/json")
    ResponseEntity<MoveResultResponse> executeMovement(
            @PathVariable("id") String gameId,
            @Valid @RequestBody ExecuteMovementRequest executeMovementRequest
    ) {
        Game game = getGameQuery.getGame(gameId);
        if (game == null)
            return ResponseEntity.notFound().build();

        ExecuteMovementCommand executeMovementCommand = new ExecuteMovementCommand(
                executeMovementRequest.getPit(), game);

        MoveResult moveResult = executeMovementUseCase.executeMovement(executeMovementCommand);
        if (moveResult == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(MoveResultResponse.of(moveResult));
    }

}

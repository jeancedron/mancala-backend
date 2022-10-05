package com.jeancedron.mancala.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeancedron.mancala.adapter.in.web.domain.CreateGameRequest;
import com.jeancedron.mancala.adapter.in.web.domain.ExecuteMovementRequest;
import com.jeancedron.mancala.application.port.in.CreateGameUseCase;
import com.jeancedron.mancala.application.port.in.ExecuteMovementUseCase;
import com.jeancedron.mancala.application.port.in.GetAvailableMovesQuery;
import com.jeancedron.mancala.application.port.in.GetGameQuery;
import com.jeancedron.mancala.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameControllerTest.class)
@ComponentScan(basePackages = "com.jeancedron.mancala.adapter.in.web")
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateGameUseCase createGameUseCase;

    @MockBean
    private GetGameQuery getGameQuery;

    @MockBean
    private ExecuteMovementUseCase executeMovementUseCase;

    @MockBean
    private GetAvailableMovesQuery getAvailableMovesQuery;

    @Autowired
    private ObjectMapper objectMapper;

    int startingStones = 6;
    int[] defaultPits = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    Player playerA = new Player("Jean", 1);
    Player playerB = new Player("some-random-company.com", 2);

    @Test
    void returnHTTP200_whenCreatingGameWithValidRequest() throws Exception {
        String id = "anyId";
        CreateGameRequest createGameRequest = new CreateGameRequest(playerA.getName(), playerB.getName());
        Game game = Game.resume(id, startingStones, playerA, playerB, defaultPits, playerA, Game.GameState.STARTED);

        when(createGameUseCase.createGame(any())).thenReturn(game);
        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void returnHTTP400_whenCreatingGameWithInvalidRequest() throws Exception {
        String createGameRequest = "{}";

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createGameRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnHTTP200_whenRequestingGameWithValidRequest() throws Exception {
        String id = "anyId";
        Game game = Game.resume(id, startingStones, playerA, playerB, defaultPits, playerA, Game.GameState.STARTED);

        when(getGameQuery.getGame(any())).thenReturn(game);
        mockMvc.perform(get("/games/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void returnHTTP404_whenRequestingGameWithInValidRequest() throws Exception {
        String id = "anyInvalidId";

        when(getGameQuery.getGame(any())).thenReturn(null);
        mockMvc.perform(get("/games/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnHTTP200_whenRequestingAvailableMovesFromGame() throws Exception {
        String id = "anyId";
        Game game = Game.resume(id, startingStones, playerA, playerB, defaultPits, playerA, Game.GameState.STARTED);
        Move[] moves = new Move[]{new Move(playerA, new SmallPit(1, 6, playerA))};

        when(getGameQuery.getGame(any())).thenReturn(game);
        when(getAvailableMovesQuery.getAvailableMoves(any())).thenReturn(moves);

        mockMvc.perform(get("/games/{id}/moves", id))
                .andExpect(status().isOk());
    }

    @Test
    void returnHTTP404_whenRequestingAvailableMovesFromInvalidGame() throws Exception {
        String id = "anyInvalidId";

        when(getGameQuery.getGame(any())).thenReturn(null);
        when(getAvailableMovesQuery.getAvailableMoves(any())).thenReturn(new Move[]{});

        mockMvc.perform(get("/games/{id}/moves", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnHTTP200_whenExecutingMovementWithValidRequest() throws Exception {
        String id = "anyId";
        Game game = Game.resume(id, startingStones, playerA, playerB, defaultPits, playerA, Game.GameState.STARTED);
        MoveResult moveResult = new MoveResult(false, playerB, null);

        ExecuteMovementRequest movementRequest = new ExecuteMovementRequest(2);

        when(getGameQuery.getGame(any())).thenReturn(game);
        when(executeMovementUseCase.executeMovement(any())).thenReturn(moveResult);
        mockMvc.perform(post("/games/{id}/moves", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movementRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void returnHTTP400_whenExecutingMovementWithInvalidGame() throws Exception {
        String id = "invalidGame";
        ExecuteMovementRequest movementRequest = new ExecuteMovementRequest(2);

        when(getGameQuery.getGame(any())).thenReturn(null);
        mockMvc.perform(post("/games/{id}/moves", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movementRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnHTTP400_whenExecutingMovementWithInvalidRequest() throws Exception {
        String id = "anyId";
        Game game = Game.resume(id, startingStones, playerA, playerB, defaultPits, playerA, Game.GameState.STARTED);

        ExecuteMovementRequest movementRequest = new ExecuteMovementRequest(999);

        when(getGameQuery.getGame(any())).thenReturn(game);
        when(executeMovementUseCase.executeMovement(any())).thenReturn(null);
        mockMvc.perform(post("/games/{id}/moves", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movementRequest)))
                .andExpect(status().isBadRequest());
    }

}

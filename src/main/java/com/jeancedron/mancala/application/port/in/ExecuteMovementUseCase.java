package com.jeancedron.mancala.application.port.in;

import com.jeancedron.mancala.domain.MoveResult;

public interface ExecuteMovementUseCase {

    MoveResult executeMovement(ExecuteMovementCommand executeMovementCommand);

}

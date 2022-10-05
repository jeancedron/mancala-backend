package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteMovementRequest {

    @NotNull(message = "pit must not be null")
    @JsonProperty("pit")
    private int pit;

}

package com.jeancedron.mancala.adapter.in.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<String> detail;

}

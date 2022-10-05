package com.jeancedron.mancala.adapter.in.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "game")
public class GameConfigurationProperties {
    private int startingStones;
}

package com.jeancedron.mancala;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MancalaApplicationTest {

    @Test
    void contextLoads() {
        assertAll(() -> MancalaApplication.main(new String[]{}));
    }

}

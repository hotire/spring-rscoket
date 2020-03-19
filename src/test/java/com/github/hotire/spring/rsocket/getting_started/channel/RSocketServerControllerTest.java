package com.github.hotire.spring.rsocket.getting_started.channel;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RSocketServerControllerTest {

    @Test
    void create() {
        // when then
        assertThat(new RSocketServerController("", 0)).isNotNull();
        assertThat(new RSocketServerController("", 0, Map.of())).isNotNull();
    }
}
package com.github.hotire.spring.rsocket.getting_started.channel;

import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RSocketClientControllerTest {
    @Test
    void addMessages() {
        // given
        final List<Payload> payloads = mock(List.class);
        final RSocketClientController clientController = new RSocketClientController(payloads);

        // when
        clientController.addMessages(Lists.newArrayList(DefaultPayload.create("hello")));

        // then
        verify(payloads, times(1)).addAll(any());
    }
}
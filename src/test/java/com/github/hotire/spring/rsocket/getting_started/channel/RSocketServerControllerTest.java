package com.github.hotire.spring.rsocket.getting_started.channel;

import io.rsocket.Payload;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RSocketServerControllerTest {

    @Test
    void create() {
        // when then
        assertThat(new RSocketServerController("", 0)).isNotNull();
        assertThat(new RSocketServerController("", 0, Map.of())).isNotNull();
    }

    @Test
    void subscribe() {
        // given
        final Subscriber<? super Payload> subscriber = mock(Subscriber.class);
        final RSocketServerController rSocketServerController = new RSocketServerController("", 0);

        // when
        rSocketServerController.subscribe(subscriber);

        // then
        verify(subscriber, times(1)).onNext(any());
        verify(subscriber, times(1)).onComplete();
    }
}
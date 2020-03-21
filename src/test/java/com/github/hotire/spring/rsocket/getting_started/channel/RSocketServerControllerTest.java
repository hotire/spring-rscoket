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

    @Test
    void subscribe_error() {
        // given
        final Subscriber<? super Payload> subscriber = mock(Subscriber.class);
        final RSocketServerController rSocketServerController = new RSocketServerController("", 0) {
            @Override
            protected Runnable getRunnable() {
                return () -> {throw new RuntimeException();};
            }
        };

        // when
        rSocketServerController.subscribe(subscriber);

        // then
        verify(subscriber, times(1)).onError(any());
    }


    @Test
    void send() {
        // given
        final Runnable runnable = mock(Runnable.class);
        final RSocketServerController rSocketServerController = new RSocketServerController("", 0) {
            @Override
            protected Runnable getRunnable() {
                return runnable;
            }
        };
        // when
        rSocketServerController.send();

        // then
        verify(runnable, times(1)).run();
    }
}
package com.github.hotire.spring.rsocket.getting_started.request_stream;

import com.github.hotire.spring.rsocket.getting_started.Server;
import io.rsocket.util.DefaultPayload;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ReqStreamClientTest {

    @Test
    void getDataStream() {
        // given
        final int port = FreePortFinder.findFreeLocalPort();
        final Server server = new Server(port);
        server.getData().add(DefaultPayload.create("DATA_STREAM_NAME"));
        final ReqStreamClient reqStreamClient = new ReqStreamClient(port);

        // when
        final Flux<Float> result = reqStreamClient.getDataStream();

        // then
        StepVerifier.create(result)
                    .expectNextCount(2)
                    .verifyComplete();
    }
}
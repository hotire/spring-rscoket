package com.github.hotire.spring.rsocket.getting_started.request_reponse;

import com.github.hotire.spring.rsocket.getting_started.Server;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReqResClientTest {

    @Test
    void callBlocking() throws InterruptedException {
        // given
        final String message = "hello";
        final int port = FreePortFinder.findFreeLocalPort();
        final Server server = new Server(port);
        final ReqResClient reqResClient = new ReqResClient(port);

        // when
        final Mono<String> mono = reqResClient.callBlocking(message)
                                              .doOnTerminate(() -> {
                                                reqResClient.dispose();
                                                server.dispose();
                                              });

        // then
        StepVerifier.create(mono).expectNext(message).verifyComplete();
    }
}
package com.github.hotire.spring.rsocket.getting_started;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Server {

    private final Disposable server;
    @Getter
    private final List<Payload> data = new ArrayList<>();

    public Server(final int port) {
        this.server = RSocketFactory.receive()
                                    .acceptor((setupPayload, reactiveSocket) -> Mono.just(new RSocketImpl()))
                                    .transport(TcpServerTransport.create("localhost", port))
                                    .start()
                                    .subscribe();
    }

    public void dispose() {
        this.server.dispose();
    }

    private class RSocketImpl extends AbstractRSocket {
        @Override
        public Mono<Payload> requestResponse(Payload payload) {
            return Mono.just(payload);
        }
        @Override
        public Mono<Void> fireAndForget(Payload payload) {
            try {
                log.info("payload : {}", payload);
                data.add(payload);
                return Mono.empty();
            } catch (Exception x) {
                return Mono.error(x);
            }
        }
    }
}

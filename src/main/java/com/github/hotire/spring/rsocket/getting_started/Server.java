package com.github.hotire.spring.rsocket.getting_started;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public class Server {
    private final Disposable server;

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
            payload.release();
            return Mono.just(payload);
        }
    }
}

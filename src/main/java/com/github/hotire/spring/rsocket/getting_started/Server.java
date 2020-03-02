package com.github.hotire.spring.rsocket.getting_started;

import com.github.hotire.spring.rsocket.getting_started.channel.RSocketController;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
public class Server {

    private final Disposable server;
    @Getter
    private final List<Payload> data = new ArrayList<>();

    private final RSocketController rSocketController;


    public Server(final int port) {
        this.server = RSocketFactory.receive()
                                    .acceptor((setupPayload, reactiveSocket) -> Mono.just(new RSocketImpl()))
                                    .transport(TcpServerTransport.create("localhost", port))
                                    .start()
                                    .subscribe();
        rSocketController = new RSocketController("Server");
        rSocketController.setExecutorService(Executors.newFixedThreadPool(3));
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
        @Override
        public Flux<Payload> requestStream(Payload payload) {
            data.add(payload);
            return Flux.fromIterable(data);
        }
        @SuppressWarnings("CallingSubscribeInNonBlockingScope")
        @Override
        public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
            Flux.from(payloads).subscribe(rSocketController::process);
            return Flux.from(rSocketController);
        }
    }
}

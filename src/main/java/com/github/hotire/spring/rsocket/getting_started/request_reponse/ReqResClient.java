package com.github.hotire.spring.rsocket.getting_started.request_reponse;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;

public class ReqResClient {

    private final RSocket socket;

    public ReqResClient(final int port) {
        this.socket = RSocketFactory.connect()
                                    .transport(TcpClientTransport.create("localhost", port))
                                    .start()
                                    .block();
    }

    public Mono<String> callBlocking(String string) {
        return socket
                .requestResponse(DefaultPayload.create(string))
                .map(Payload::getDataUtf8);
    }

    public void dispose() {
        this.socket.dispose();
    }
}
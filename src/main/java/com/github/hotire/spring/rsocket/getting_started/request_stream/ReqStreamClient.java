package com.github.hotire.spring.rsocket.getting_started.request_stream;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;

public class ReqStreamClient {

    private final RSocket socket;

    public ReqStreamClient(int port) {
        this.socket = RSocketFactory.connect()
                                    .transport(TcpClientTransport.create("localhost", port))
                                    .start()
                                    .block();
    }

    public Flux<Float> getDataStream() {
        return socket
                .requestStream(DefaultPayload.create("DATA_STREAM_NAME"))
                .map(Payload::getData)
                .map(ByteBuffer::getFloat);
    }

    public void dispose() {
        this.socket.dispose();
    }
}
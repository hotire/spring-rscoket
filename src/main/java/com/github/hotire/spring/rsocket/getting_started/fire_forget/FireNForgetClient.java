package com.github.hotire.spring.rsocket.getting_started.fire_forget;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


public class FireNForgetClient {
    private final RSocket socket;
    @Getter
    private final List<Float> data;

    private static final int DATA_LENGTH = 30;

    public FireNForgetClient(final int port) {
        this.socket = RSocketFactory.connect()
                                    .transport(TcpClientTransport.create("localhost", port))
                                    .start()
                                    .block();
        this.data = Collections.unmodifiableList(generateData());
    }

    /**
     * Send binary velocity (float) every 50ms
     */
    public void sendData() {
        Flux.interval(Duration.ofMillis(50))
            .take(data.size())
            .map(this::createFloatPayload)
            .flatMap(socket::fireAndForget)
            .blockLast();
    }

    /**
     * Create a binary payload containing a single float value
     */
    private Payload createFloatPayload(Long index) {
        float velocity = data.get(index.intValue());
        ByteBuffer buffer = ByteBuffer.allocate(4).putFloat(velocity);
        buffer.rewind();
        return DefaultPayload.create(buffer);
    }

    private List<Float> generateData() {
        return IntStream.rangeClosed(0, 30)
                 .mapToObj(i -> (float)Math.random())
                 .collect(toList());
    }

    public void dispose() {
        this.socket.dispose();
    }
}

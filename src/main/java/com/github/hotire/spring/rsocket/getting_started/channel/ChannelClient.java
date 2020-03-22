package com.github.hotire.spring.rsocket.getting_started.channel;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.github.hotire.spring.rsocket.getting_started.channel.ChanelConstants.LOCAL;

public class ChannelClient {

    private final RSocket socket;
    @Getter
    private final RSocketClientController clientController;

    public ChannelClient(final int port) {
        this.socket = RSocketFactory.connect()
                                    .transport(TcpClientTransport.create(LOCAL, port))
                                    .start()
                                    .block();
        this.clientController = new RSocketClientController(new ArrayList<>());
    }

    public void process(final List<Payload> messages) {
        clientController.addMessages(messages);
        socket.requestChannel(clientController)
              .doOnNext(clientController::process)
              .blockLast();
    }

    public void dispose() {
        this.socket.dispose();
    }
}
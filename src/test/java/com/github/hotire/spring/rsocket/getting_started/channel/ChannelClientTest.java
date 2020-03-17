package com.github.hotire.spring.rsocket.getting_started.channel;

import com.github.hotire.spring.rsocket.getting_started.Server;
import io.rsocket.util.DefaultPayload;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.Test;

import java.util.List;

class ChannelClientTest {

    @Test
    void process_() {
        // given
        final int port = FreePortFinder.findFreeLocalPort();
        final Server server = new Server(port);
        final ChannelClient channelClient = new ChannelClient(port);

        // when
        channelClient.process(List.of(DefaultPayload.create("hello")));

        // then
        channelClient.dispose();
        server.dispose();
    }
}
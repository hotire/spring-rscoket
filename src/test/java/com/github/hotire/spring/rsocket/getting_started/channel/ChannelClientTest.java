package com.github.hotire.spring.rsocket.getting_started.channel;

import com.github.hotire.spring.rsocket.getting_started.Server;
import io.rsocket.util.DefaultPayload;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChannelClientTest {

    @Test
    void process() {
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

    @Test
    void getClientController() {
        // given
        final int port = FreePortFinder.findFreeLocalPort();
        final Server server = new Server(port);
        final ChannelClient channelClient = new ChannelClient(port);

        // when
        final RSocketClientController result = channelClient.getClientController();

        // then
        assertThat(result).isInstanceOf(Publisher.class);
        channelClient.dispose();
        server.dispose();
    }
}
package com.github.hotire.spring.rsocket.getting_started.fire_forget;

import com.github.hotire.spring.rsocket.getting_started.Server;
import io.rsocket.Payload;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FireNForgetClientTest {

    @Test
    void sendData() throws InterruptedException {
        // given
        final int port = FreePortFinder.findFreeLocalPort();
        final Server server = new Server(port);
        final FireNForgetClient fireNForgetClient = new FireNForgetClient(port);

        // when
        fireNForgetClient.sendData();
        final List<Payload> result = server.getData();

        // then
        assertThat(result.size()).isEqualTo(30);
        fireNForgetClient.dispose();
        server.dispose();
    }
}
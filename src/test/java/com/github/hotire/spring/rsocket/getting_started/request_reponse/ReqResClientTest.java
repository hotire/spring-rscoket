package com.github.hotire.spring.rsocket.getting_started.request_reponse;

import com.github.hotire.spring.rsocket.getting_started.Server;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.Test;

class ReqResClientTest {

    @Test
    void callBlocking() {
        final int port = FreePortFinder.findFreeLocalPort();
        final Server server = new Server(port);
        final ReqResClient reqResClient = new ReqResClient(port);
        reqResClient.callBlocking("hello")
                    .doOnTerminate(() -> {
                        server.dispose();
                        reqResClient.dispose();
                    })
                    .subscribe();
    }
}
package com.github.hotire.spring.rsocket.request_reponse;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
public class ClientConfiguration {

    @Bean
    public RSocket rSocket() {
        return RSocketFactory
                .connect()
                .mimeType(APPLICATION_JSON_VALUE, APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.wrap(rSocket(), APPLICATION_JSON, APPLICATION_JSON,  rSocketStrategies);
    }
}
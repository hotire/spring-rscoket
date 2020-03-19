package com.github.hotire.spring.rsocket.getting_started.channel;

import io.rsocket.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
public class RSocketClientController implements Publisher<Payload> {

    private final AtomicBoolean active;
    private final List<Payload> messages;
    private final Map<String, BiConsumer<String, RSocketClientController>> handlerMap;
    private Subscriber<? super Payload> subscriber;

    public RSocketClientController(final List<Payload> messages) {
        this(messages, Map.of("exit", (data, rSocketClientController)-> rSocketClientController.active.set(false)));
    }

    public RSocketClientController(final List<Payload> messages, final Map<String, BiConsumer<String, RSocketClientController>> handlerMap) {
        this.messages = messages;
        this.active = new AtomicBoolean(true);
        this.handlerMap = handlerMap;
    }

    @Override
    public void subscribe(final Subscriber<? super Payload> subscriber) {
        this.subscriber = subscriber;
        try {
            messages.forEach(this::send);
            subscriber.onComplete();
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }

    public void addMessages(final List<Payload> payloads) {
        this.messages.addAll(payloads);
    }

    public void process(final Payload payload) {
        final String data = payload.getDataUtf8();
        log.info("data : {}", data);
        Optional.ofNullable(handlerMap.get(data)).ifPresent(handler -> handler.accept(data,this));
    }

    protected void send(final Payload message) {
        try {
            Thread.sleep(1000);
            subscriber.onNext(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

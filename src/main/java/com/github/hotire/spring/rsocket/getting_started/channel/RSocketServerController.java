package com.github.hotire.spring.rsocket.getting_started.channel;


import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.github.hotire.spring.rsocket.getting_started.channel.ChanelConstants.EXIT;

@Slf4j
@RequiredArgsConstructor
public class RSocketServerController implements Publisher<Payload> {
    private final String name;
    @Getter
    private final AtomicInteger count;
    private final Map<String, Consumer<RSocketServerController>> processMap;

    public RSocketServerController(final String name, int count) {
        this(name, count, Map.of(EXIT, server -> server.getCount().set(0)));
    }

    public RSocketServerController(final String name, int count, Map<String, Consumer<RSocketServerController>> processMap) {
        this.name = name;
        this.count = new AtomicInteger(count);
        this.processMap = processMap;
    }

    @Setter
    private ExecutorService executorService;
    @Getter
    private Subscriber<? super Payload> subscriber;

    protected Optional<ExecutorService> getExecutorService() {
        return Optional.ofNullable(executorService);
    }

    @Override
    public void subscribe(final Subscriber<? super Payload> subscriber) {
        this.subscriber = subscriber;
        try {
            send();
        } catch (Exception e) {
            getSubscriber().onError(e);
        }
    }

    protected void send() {
        final Runnable runnable = getRunnable();
        getExecutorService().ifPresentOrElse(es -> es.execute(runnable), runnable);
    }

    protected Runnable getRunnable() {
        return () -> {
            while (true) {
                if (getCount().get() == 0) {
                    getSubscriber().onNext(DefaultPayload.create(EXIT));
                    getSubscriber().onComplete();
                    return;
                }
                getSubscriber().onNext(DefaultPayload.create("count :" + getCount().getAndDecrement()));
                try { Thread.sleep(1000); } catch (InterruptedException ignore) { }
            }
        };
    }

    public void process(final Payload payload) {
        final String data = payload.getDataUtf8();
        log.info(name + ", data :{}", data);
        Optional.ofNullable(processMap.get(data)).ifPresent(consumer -> consumer.accept(this));
    }
}

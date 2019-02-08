package com.prashanth.spring.rsocket.realtime;

import io.rsocket.*;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class Pong implements SocketAcceptor, Ordered, ApplicationListener<ApplicationReadyEvent> {

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {

        AbstractRSocket abstractRSocket = new AbstractRSocket() {
            @Override
            public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
                return Flux.from(payloads)
                        .map(Payload::getDataUtf8)
                        .doOnNext(str -> log.info("received "+str+ "in "+getClass().getName())).map(null);

            }
        };
        return null;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        RSocketFactory
                .receive()
                .acceptor(this)
                .transport(TcpServerTransport.create(8000))
                .start()
                .subscribe();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

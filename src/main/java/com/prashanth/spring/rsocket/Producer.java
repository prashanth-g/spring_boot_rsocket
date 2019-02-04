package com.prashanth.spring.rsocket;

import io.rsocket.*;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@Component
public class Producer implements Ordered, ApplicationListener<ApplicationReadyEvent> {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    Flux<String> notifications(String name) {
        return Flux
                .fromStream(Stream.generate(() -> "Halo - "+name+" "+ Instant.now().toString()))
                .delayElements(Duration.ofSeconds(1));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        SocketAcceptor socketAcceptor = (connectionSetupPayload, sender) -> {
                AbstractRSocket abstractRSocket = new AbstractRSocket() {
                    public Flux<Payload> requestStream(Payload payload) {
                        return notifications(payload.getDataUtf8())
                                .map(DefaultPayload::create);
                    }
                };
                return Mono.just(abstractRSocket);
        };

        TcpServerTransport tcpServerTransport = TcpServerTransport.create(7000);
        RSocketFactory
                .receive()
                .acceptor(socketAcceptor)
                .transport(tcpServerTransport)
                .start()
                .block();
    }
}

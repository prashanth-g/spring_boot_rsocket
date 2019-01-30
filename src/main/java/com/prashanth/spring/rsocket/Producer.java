package com.prashanth.spring.rsocket;

import io.rsocket.*;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Producer implements Ordered, ApplicationListener<ApplicationReadyEvent> {
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        SocketAcceptor socketAcceptor = new SocketAcceptor() {
            @Override
            public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
                AbstractRSocket abstractRSocket = new AbstractRSocket() {
                    public Flux<Payload> requestStream(Payload payload) {
                        return super.requestStream(payload);
                    }
                };
                return Mono.just(abstractRSocket);
            }
        };
        TcpServerTransport tcpServerTransport = TcpServerTransport.create(700);
        RSocketFactory
                .receive()
                .acceptor(socketAcceptor)
                .transport(tcpServerTransport)
                .start()
                .block();
    }
}

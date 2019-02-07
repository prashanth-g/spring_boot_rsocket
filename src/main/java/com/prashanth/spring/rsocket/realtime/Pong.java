package com.prashanth.spring.rsocket.realtime;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class Pong implements SocketAcceptor, Ordered, ApplicationListener<ApplicationReadyEvent> {

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        return null;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}

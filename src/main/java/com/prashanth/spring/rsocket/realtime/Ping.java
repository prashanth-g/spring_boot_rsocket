package com.prashanth.spring.rsocket.realtime;

import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Log4j2
@Component
public class Ping implements Ordered, ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Starting "+this.getClass().getName());
        RSocketFactory
                .connect()
                .transport(TcpClientTransport.create(8000))
                .start()
                .flatMapMany(rSocket ->
                        rSocket.requestChannel (
                            Flux.interval(Duration.ofSeconds(1)).map(i -> DefaultPayload.create("ping")))
                        .map(Payload::getDataUtf8)
                        .doOnNext(str -> log.info("received " + str + "in" + getClass().getName()))
                        .take(10)
                        .doFinally(signal -> rSocket.dispose())
                ).then().block();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

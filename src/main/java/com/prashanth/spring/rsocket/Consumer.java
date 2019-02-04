package com.prashanth.spring.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Consumer implements Ordered, ApplicationListener<ApplicationReadyEvent> {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        RSocketFactory.connect()
                .transport(TcpClientTransport.create(7000))
                .start()
                .flatMapMany(sender ->
                        sender.requestStream(DefaultPayload.create("RSocket")).map(Payload::getDataUtf8))
                .subscribe(result -> log.info("Processing result" + result));
    }
}

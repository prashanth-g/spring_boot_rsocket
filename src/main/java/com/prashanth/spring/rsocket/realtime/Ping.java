package com.prashanth.spring.rsocket.realtime;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public class Ping implements Ordered, ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}

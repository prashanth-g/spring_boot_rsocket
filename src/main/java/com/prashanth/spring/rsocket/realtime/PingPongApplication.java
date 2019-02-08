package com.prashanth.spring.rsocket.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PingPongApplication {

    static String reply(String in) {
        if(in.equalsIgnoreCase("ping")) return "pong";
        if(in.equalsIgnoreCase("pong")) return "ping";
        throw new IllegalArgumentException("Incoming value must be either ping or pong");
    }

    public static void main(String[] args) {
        SpringApplication.run(PingPongApplication.class, args);
    }
}

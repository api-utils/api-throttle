package com.nobodyhub.transcendence.api.throttle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(ApiThrottleConfiguration.class)
public class ApiThrottleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiThrottleApplication.class, args);
    }
}

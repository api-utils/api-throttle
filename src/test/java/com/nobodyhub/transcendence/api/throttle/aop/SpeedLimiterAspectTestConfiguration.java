package com.nobodyhub.transcendence.api.throttle.aop;

import com.nobodyhub.transcendence.api.throttle.ApiThrottleConfiguration;
import com.nobodyhub.transcendence.api.throttle.anno.SpeedLimited;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@Import(ApiThrottleConfiguration.class)
public class SpeedLimiterAspectTestConfiguration {
    @Bean
    public TestClass testClass() {
        return new TestClass();
    }


    public static class TestClass {
        @SpeedLimited(bucket = "TestBucket")
        public void execute(List<Long> executions) {
            executions.add(System.currentTimeMillis());
        }
    }
}

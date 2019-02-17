package com.nobodyhub.transcendence.api.throttle;

import com.nobodyhub.transcendence.api.throttle.config.RedisConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@EnableCaching
@Import(RedisConfiguration.class)
public class ApiThrottleConfiguration {
}

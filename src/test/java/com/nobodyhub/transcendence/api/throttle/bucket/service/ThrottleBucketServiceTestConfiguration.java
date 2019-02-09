package com.nobodyhub.transcendence.api.throttle.bucket.service;

import com.nobodyhub.transcendence.api.throttle.ApiThrottleConfiguration;
import com.nobodyhub.transcendence.api.throttle.config.RedisConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisConfiguration.class)
@ComponentScan(basePackageClasses = ApiThrottleConfiguration.class)
public class ThrottleBucketServiceTestConfiguration {
}

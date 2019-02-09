package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.nobodyhub.transcendence.api.throttle.ApiThrottleConfiguration;
import com.nobodyhub.transcendence.api.throttle.config.RedisConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableCaching
@SpringBootApplication(scanBasePackageClasses = ApiThrottleConfiguration.class)
@Import(RedisConfiguration.class)
public class ThrottlePolicyServiceTestConfiguration {

}

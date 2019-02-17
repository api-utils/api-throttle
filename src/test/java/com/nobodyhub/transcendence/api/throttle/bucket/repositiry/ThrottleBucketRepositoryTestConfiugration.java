package com.nobodyhub.transcendence.api.throttle.bucket.repositiry;

import com.nobodyhub.transcendence.api.throttle.ApiThrottleConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApiThrottleConfiguration.class)
@ComponentScan(basePackageClasses = ApiThrottleConfiguration.class)
public class ThrottleBucketRepositoryTestConfiugration {
}

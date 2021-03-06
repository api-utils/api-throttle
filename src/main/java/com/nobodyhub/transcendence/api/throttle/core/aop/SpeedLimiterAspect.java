package com.nobodyhub.transcendence.api.throttle.core.aop;

import com.nobodyhub.transcendence.api.throttle.bucket.service.ThrottleBucketService;
import com.nobodyhub.transcendence.api.throttle.core.anno.BlockPolicy;
import com.nobodyhub.transcendence.api.throttle.core.anno.SpeedLimited;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SpeedLimiterAspect {
    private final ThrottleBucketService bucketService;

    @Around("@annotation(com.nobodyhub.transcendence.api.throttle.core.anno.SpeedLimited)")
    public Object speedLimited(ProceedingJoinPoint joinPoint) throws Throwable {
        SpeedLimited speedLimited = ((MethodSignature) joinPoint.getSignature())
                .getMethod().getAnnotation(SpeedLimited.class);
        BlockPolicy blockPolicy = speedLimited.whenBlocked();
        Object result = null;
        if (blockPolicy.apply(bucketService, speedLimited)) {
            result = joinPoint.proceed();
        }
        return result;
    }
}

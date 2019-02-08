package com.nobodyhub.transcendence.api.throttle.core.aop;

import com.nobodyhub.transcendence.api.throttle.bucket.service.ThrottleBucketService;
import com.nobodyhub.transcendence.api.throttle.core.anno.BlockPolicy;
import com.nobodyhub.transcendence.api.throttle.core.anno.SpeedLimited;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SpeedLimiterAspect {
    private final ThrottleBucketService bucketService;

    public SpeedLimiterAspect(ThrottleBucketService bucketService) {
        this.bucketService = bucketService;
    }

    //TODO: handle SpeedLimits
    @Around("@annotation(com.nobodyhub.transcendence.api.throttle.core.anno.SpeedLimited)")
    public Object limitSpeed(ProceedingJoinPoint joinPoint) throws Throwable {
        SpeedLimited speedLimited = ((MethodSignature) joinPoint.getSignature())
                .getMethod().getAnnotation(SpeedLimited.class);
        String bucket = speedLimited.bucket();
        BlockPolicy blockPolicy = speedLimited.whenBlocked();
        int retry = speedLimited.retry();
        long delay = speedLimited.retryDelay();

        Object result = null;
        boolean keepWaiting = true;
        while (keepWaiting && (retry >= 0)) {
            String execToken = bucketService.getSetBucket(bucket);
            if (bucketService.checkExecToken(bucket, execToken)) {
                result = joinPoint.proceed();
                keepWaiting = false;
            } else {
                switch (blockPolicy) {
                    case SKIP: {
                        keepWaiting = false;
                        break;
                    }
                    case WAIT: {
                        keepWaiting = true;
                        break;
                    }
                }
            }
            Thread.sleep(delay);
            retry--;
        }
        return result;
    }
}

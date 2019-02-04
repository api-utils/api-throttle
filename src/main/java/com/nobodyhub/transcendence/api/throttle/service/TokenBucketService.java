package com.nobodyhub.transcendence.api.throttle.service;

import com.nobodyhub.transcendence.api.throttle.domain.BucketPolicy;
import com.nobodyhub.transcendence.api.throttle.domain.BucketStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBucketService {
    private final StringRedisTemplate redisTemplate;

    public TokenBucketService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void createBucket(final String bucket) {
        SessionCallback<String> callback = new SessionCallback<String>() {
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch(bucket);
                BucketStatus status = getBucketStatus(redisOperations, bucket);
                BucketPolicy policy = getBucketPolicy();
                // TODO: judge policy and status
                redisOperations.exec();
                return null;
            }
        };
        this.redisTemplate.execute(callback);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private BucketStatus getBucketStatus(RedisOperations redisOperations,
                                         String bucket) {
        // bucket status
        Integer nToken = (Integer) redisOperations.<String, String>boundHashOps(bucket).get("nToken");
        Long lastRequest = (Long) redisOperations.<String, String>boundHashOps(bucket).get("lastRequest");

        // bucket windowed history
        Long nWindowed = redisOperations.boundSetOps(bucket + "_windowed").size();
        return new BucketStatus(nToken, lastRequest, nWindowed);
    }

    private BucketPolicy getBucketPolicy() {
        // TODO: get bucket policy
        return null;
    }


}

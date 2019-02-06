package com.nobodyhub.transcendence.api.throttle.service;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.domain.BucketPolicy;
import com.nobodyhub.transcendence.api.throttle.domain.BucketStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenBucketService {
    private final StringRedisTemplate redisTemplate;

    public TokenBucketService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void createBucket(final String bucket) {
        SessionCallback<String> callback = new SessionCallback<String>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public String execute(RedisOperations operations) throws DataAccessException {
                operations.watch(bucket + "_policy");
                BucketPolicy policy = getBucketPolicy(operations, bucket);
                initBucketStatus(operations, bucket, policy);
                operations.exec();
                return null;
            }
        };
        this.redisTemplate.execute(callback);
    }

    public String getSetBucket(final String bucket) {
        SessionCallback<String> callback = new SessionCallback<String>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public String execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.watch(Lists.newArrayList(
                        bucket + "_nToken",
                        bucket + "_lastRequest",
                        bucket + "_windowed",
                        bucket + "_policy"
                ));
                Long timestamp = System.currentTimeMillis();
                String execToken = UUID.randomUUID().toString();
                redisOperations.multi();
                BucketPolicy policy = getBucketPolicy(redisOperations, bucket);
                BucketStatus status = getBucketStatus(redisOperations, bucket, timestamp, policy);
                if (policy.check(timestamp, status)) {
                    updateBucketStatus(redisOperations, bucket, timestamp, status, policy);
                    updateExecToken(redisOperations, bucket, execToken);
                    redisOperations.exec();
                }
                redisOperations.discard();
                return execToken;
            }
        };
        return this.redisTemplate.execute(callback);
    }


    @SuppressWarnings({"unchecked"})
    private BucketStatus getBucketStatus(RedisOperations redisOperations,
                                         String bucket,
                                         Long timestamp,
                                         BucketPolicy policy) {
        // bucket tokens
        Integer nToken = (Integer) redisOperations.boundValueOps(bucket + "_nToken").get();
        //bucket last request
        Long lastRequest = (Long) redisOperations.boundValueOps(bucket + "_lastRequest").get();
        // bucket windowed history
        redisOperations.boundZSetOps(bucket + "_windowed").removeRangeByScore(0, timestamp - policy.getWindow());
        Long nWindowed = redisOperations.boundZSetOps(bucket + "_windowed").size();

        return new BucketStatus(nToken, lastRequest, nWindowed);
    }

    @SuppressWarnings({"unchecked"})
    private BucketPolicy getBucketPolicy(RedisOperations redisOperations,
                                         String bucket) {
        Long window = (Long) redisOperations.boundHashOps(bucket + "_policy").get("window");
        Long nToken = (Long) redisOperations.boundHashOps(bucket + "_policy").get("nToken");
        Long interval = (Long) redisOperations.boundHashOps(bucket + "_policy").get("interval");
        return new BucketPolicy(window, nToken, interval);
    }

    @SuppressWarnings({"unchecked"})
    private void initBucketStatus(RedisOperations redisOperations,
                                  String bucket,
                                  BucketPolicy policy) {
        redisOperations.boundValueOps(bucket + "_nToken").set(policy.getNToken());
        redisOperations.boundValueOps(bucket + "_lastRequest").set(0);
        redisOperations.boundZSetOps(bucket + "_windowed").add(0, 0);
    }

    @SuppressWarnings({"unchecked"})
    private void updateBucketStatus(RedisOperations redisOperations,
                                    String bucket,
                                    Long timestamp,
                                    BucketStatus status,
                                    BucketPolicy policy) {
        redisOperations.boundValueOps(bucket + "_nToken")
                .set(policy.assignToken(timestamp - status.getLastRequest(), status));
        redisOperations.boundValueOps(bucket + "_lastRequest").set(timestamp);
        redisOperations.boundZSetOps(bucket + "_windowed").add(timestamp, timestamp);
    }

    @SuppressWarnings({"unchecked"})
    private void updateExecToken(RedisOperations redisOperations,
                                 String bucket,
                                 String execId) {
        redisOperations.boundValueOps(bucket + "_" + execId).set(true);
    }

    @SuppressWarnings({"unchecked"})
    public boolean checkExecToken(String bucket,
                                  String execToken) {
        return Boolean.TRUE.equals(this.redisTemplate.boundValueOps(bucket + "_" + execToken).get());
    }


}

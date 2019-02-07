package com.nobodyhub.transcendence.api.throttle.bucket.service;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketPolicy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
                // watch
                operations.watch(bucket + "_policy");
                BucketPolicy policy = getBucketPolicy(operations, bucket);
                // multi
                operations.multi();
                initBucketStatus(operations, bucket, policy);
                // exec
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
                // watch
                redisOperations.watch(Lists.newArrayList(
                        bucket + "_nToken",
                        bucket + "_lastRequest",
                        bucket + "_windowed",
                        bucket + "_policy"
                ));
                Long timestamp = System.currentTimeMillis();
                String execToken = UUID.randomUUID().toString();
                BucketPolicy policy = getBucketPolicy(redisOperations, bucket);
                BucketStatus status = getBucketStatus(redisOperations, bucket, timestamp, policy);
                // multi
                redisOperations.multi();
                if (policy.check(timestamp, status)) {
                    updateBucketStatus(redisOperations, bucket, execToken, timestamp, status, policy);
                    updateExecToken(redisOperations, bucket, execToken);
                    // exec
                    redisOperations.exec();
                } else {
                    // discard
                    redisOperations.discard();
                }
                return execToken;
            }
        };
        return this.redisTemplate.execute(callback);
    }

    public void updateBucketPolicy(String bucket, BucketPolicy policy) {
        SessionCallback<String> callback = new SessionCallback<String>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public String execute(RedisOperations operations) throws DataAccessException {
                // watch
                operations.watch(bucket + "_policy");
                // multi
                operations.multi();
                operations.boundHashOps(bucket + "_policy").put("window", String.valueOf(policy.getWindow()));
                operations.boundHashOps(bucket + "_policy").put("nToken", String.valueOf(policy.getNToken()));
                operations.boundHashOps(bucket + "_policy").put("interval", String.valueOf(policy.getInterval()));
                initBucketStatus(operations, bucket, policy);
                // exec
                operations.exec();
                return null;
            }
        };
        this.redisTemplate.execute(callback);
    }


    @SuppressWarnings({"unchecked"})
    private BucketStatus getBucketStatus(RedisOperations redisOperations,
                                         String bucket,
                                         long timestamp,
                                         BucketPolicy policy) {
        // bucket tokens
        String nToken = (String) redisOperations.boundValueOps(bucket + "_nToken").get();
        //bucket last request
        String lastRequest = (String) redisOperations.boundValueOps(bucket + "_lastRequest").get();
        // bucket windowed history
        redisOperations.boundZSetOps(bucket + "_windowed").removeRangeByScore(0, policy.getWindowUpperLimit(timestamp));
        Long nWindowed = redisOperations.boundZSetOps(bucket + "_windowed").size();

        return new BucketStatus(nToken, lastRequest, nWindowed);
    }

    @SuppressWarnings({"unchecked"})
    private BucketPolicy getBucketPolicy(RedisOperations redisOperations,
                                         String bucket) {
        String wSize = (String) redisOperations.boundHashOps(bucket + "_policy").get("window.size");
        String wLimit = (String) redisOperations.boundHashOps(bucket + "_policy").get("window.limit");
        String nToken = (String) redisOperations.boundHashOps(bucket + "_policy").get("nToken");
        String interval = (String) redisOperations.boundHashOps(bucket + "_policy").get("interval");
        return new BucketPolicy(wSize, wLimit, nToken, interval);
    }

    @SuppressWarnings({"unchecked"})
    private void initBucketStatus(RedisOperations redisOperations,
                                  String bucket,
                                  BucketPolicy policy) {
        redisOperations.boundValueOps(bucket + "_nToken").set(String.valueOf(policy.getNToken()));
    }

    @SuppressWarnings({"unchecked"})
    private void updateBucketStatus(RedisOperations redisOperations,
                                    String bucket,
                                    String execToken,
                                    long timestamp,
                                    BucketStatus status,
                                    BucketPolicy policy) {
        redisOperations.boundValueOps(bucket + "_nToken")
                .set(String.valueOf(status.getNToken() - 1));
        redisOperations.boundValueOps(bucket + "_lastRequest").set(String.valueOf(timestamp));
        redisOperations.boundZSetOps(bucket + "_windowed").add(execToken, timestamp);
    }

    @SuppressWarnings({"unchecked"})
    private void updateExecToken(RedisOperations redisOperations,
                                 String bucket,
                                 String execToken) {
        String key = bucket + "_" + execToken;
        redisOperations.boundValueOps(key).set(String.valueOf(true));
        redisOperations.expire(key, 1, TimeUnit.MINUTES);
    }

    @SuppressWarnings({"unchecked"})
    public boolean checkExecToken(String bucket,
                                  String execToken) {
        String key = bucket + "_" + execToken;
        boolean rst = Boolean.TRUE.equals(Boolean.valueOf(this.redisTemplate.boundValueOps(key).get()));
        redisTemplate.delete(key);
        return rst;
    }


}

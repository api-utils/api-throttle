package com.nobodyhub.transcendence.api.throttle.bucket.service;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.bucket.utils.BucketStatusBuilder;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.service.ThrottlePolicyService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.nobodyhub.transcendence.api.throttle.bucket.utils.ThrottleBucketNamingUtil.*;

@Service
public class ThrottleBucketService {
    private final StringRedisTemplate redisTemplate;
    private final ThrottlePolicyService policyService;

    public ThrottleBucketService(StringRedisTemplate redisTemplate,
                                 ThrottlePolicyService policyService) {
        this.redisTemplate = redisTemplate;
        this.policyService = policyService;
    }

    public void createBucket(final String bucket) {
        ThrottlePolicy policy = policyService.find(bucket);

        SessionCallback<List<Object>> callback = new SessionCallback<List<Object>>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                // watch
                operations.watch(Lists.newArrayList(
                        status(bucket)
                ));
                // multi
                operations.multi();
                initBucketStatus(operations, bucket, policy);
                // exec
                return operations.exec();
            }
        };
        //TODO: consider time limits on retry
        List<Object> rst = null;
        while (rst == null || rst.isEmpty()) {
            rst = this.redisTemplate.execute(callback);
        }
    }

    public String getSetBucket(final String bucket) {
        SessionCallback<List<Object>> callback = new SessionCallback<List<Object>>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
                ThrottlePolicy policy = policyService.find(bucket);
                // watch
                redisOperations.watch(Lists.newArrayList(
                        status(bucket),
                        window(bucket)
                ));
                long timestamp = getServerTime();
                String execToken = UUID.randomUUID().toString();
                BucketStatus status = getBucketStatus(redisOperations, bucket, timestamp, policy);
                // multi
                List<Object> ret = Lists.newArrayList();
                redisOperations.multi();
                if (policyService.check(policy, timestamp, status)) {
                    updateBucketStatus(redisOperations, bucket, execToken, timestamp, status, policy);
                    updateExecToken(redisOperations, bucket, execToken);
                    // exec
                    ret = redisOperations.exec();
                } else {
                    // discard
                    redisOperations.discard();
                }
                ret.add(execToken);
                return ret;
            }
        };

        //TODO: consider time limits on retry
        List<Object> rst = null;
        while (rst == null || rst.size() <= 1) {
            rst = this.redisTemplate.execute(callback);
        }
        return (String) rst.get(rst.size() - 1);
    }

    @SuppressWarnings({"unchecked"})
    private BucketStatus getBucketStatus(RedisOperations redisOperations,
                                         String bucket,
                                         long timestamp,
                                         ThrottlePolicy policy) {
        // bucket tokens
        BucketStatus status = (BucketStatus) redisOperations.boundValueOps(status(bucket)).get();
        // bucket windowed history
        redisOperations.boundZSetOps(window(bucket)).removeRangeByScore(0, policyService.getWindowUpperLimit(policy, timestamp));
        Long nWindowed = redisOperations.boundZSetOps(window(bucket)).size();
        if (nWindowed != null) {
            status.setNWindowed(nWindowed);
        }
        return status;
    }

    @SuppressWarnings({"unchecked"})
    private void initBucketStatus(RedisOperations redisOperations,
                                  String bucket,
                                  ThrottlePolicy policy) {
        redisOperations.boundZSetOps(window(bucket)).add(0, 0);
        redisOperations.boundValueOps(status(bucket)).set(BucketStatusBuilder.of(policy));
    }

    @SuppressWarnings({"unchecked"})
    private void updateBucketStatus(RedisOperations redisOperations,
                                    String bucket,
                                    String execToken,
                                    long timestamp,
                                    BucketStatus status,
                                    ThrottlePolicy policy) {
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
        boolean rst = Boolean.TRUE.equals(Boolean.valueOf(this.redisTemplate.boundValueOps(execution(bucket, execToken)).get()));
        redisTemplate.delete(key);
        return rst;
    }

    /**
     * Try to current time of redis server, if fails, use client time
     *
     * @return
     * @see RedisServerCommands#time()
     */
    private long getServerTime() {
        Long timestamp = RedisConnectionUtils.getConnection(redisTemplate.getRequiredConnectionFactory()).time();
        return timestamp == null ? System.currentTimeMillis() : timestamp;
    }
}

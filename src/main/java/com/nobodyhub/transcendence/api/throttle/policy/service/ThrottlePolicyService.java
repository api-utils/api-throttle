package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketPolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThrottlePolicyService {
    /**
     * check whether can proceed to execute with given status
     *
     * @param policy    policy to check
     * @param timestamp timestamp when execute
     * @param status    current status
     * @return true to proceed the execution
     */
    public boolean check(BucketPolicy policy, long timestamp, BucketStatus status) {
        boolean checkWindow = policy.getWindow() == null || check(policy.getWindow(), status.getNWindowed());
        boolean checkToken = status.getNToken() > 0;
        boolean checkInterval = policy.getInterval() == null || timestamp - status.getLastRequest() > policy.getInterval() * 1000;
        return checkInterval && checkWindow && checkToken;
    }

    /**
     * @param policy
     * @param timestamp
     * @return
     */
    public long getWindowUpperLimit(BucketPolicy policy, long timestamp) {
        if (policy.getWindow() != null) {
            return getEarliest(policy.getWindow(), timestamp);
        }
        return timestamp;
    }

    /**
     * Get the earliest timestamp that should be retained in the window
     *
     * @param timestamp
     * @return
     */
    private long getEarliest(BucketWindow window, long timestamp) {
        long earliest = timestamp - window.getSize();
        return earliest > 0 ? earliest : 0;
    }

    /**
     * Check whether number of executions in the window is within the limit or not
     *
     * @param nWindowed number of executions in the window
     * @return
     */
    private boolean check(BucketWindow window, long nWindowed) {
        return nWindowed < window.getLimit();
    }
}

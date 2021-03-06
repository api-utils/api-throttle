
package com.nobodyhub.transcendence.api.throttle.policy.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThrottlePolicyUtil {

    /**
     * check whether can proceed to execute with given status
     *
     * @param policy    policy to check
     * @param timestamp timestamp when execute
     * @param status    current status
     * @return true to proceed the execution
     */
    public static boolean check(ThrottlePolicy policy, long timestamp, BucketStatus status) {
        boolean checkWindow = policy.getWindow() == null || check(policy.getWindow(), status.getNWindowed());
        boolean checkToken = status.getNToken() > 0;
        boolean checkInterval = timestamp - status.getLastRequest() >= policy.getInterval();
        return checkInterval && checkWindow && checkToken;
    }

    /**
     * return the lower limit(earliest) for the window
     *
     * @param policy
     * @param timestamp
     * @return
     */
    public static long getWindowLowerLimit(@NonNull ThrottlePolicy policy, long timestamp) {
        if (policy.getWindow() != null) {
            return getEarliest(policy.getWindow(), timestamp);
        }
        return timestamp;
    }

    /**
     * Get the earliest timestamp that should be retained in the window
     *
     * @param timestamp current time in millisecond
     * @return
     */
    private static long getEarliest(@NonNull BucketWindow window, long timestamp) {
        long earliest = timestamp - window.getSize();
        return earliest > 0 ? earliest : 0;
    }

    /**
     * Check whether number of executions in the window is within the limit or not
     *
     * @param nWindowed number of executions in the window
     * @return
     */
    private static boolean check(BucketWindow window, long nWindowed) {
        return nWindowed < window.getLimit();
    }
}

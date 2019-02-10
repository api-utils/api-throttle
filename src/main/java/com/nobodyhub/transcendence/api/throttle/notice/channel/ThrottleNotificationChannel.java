package com.nobodyhub.transcendence.api.throttle.notice.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ThrottleNotificationChannel {
    /**
     * Notification sent when bucket status error
     */
    String NOTIFY_BUCKET_STATUS_ERROR = "api-throttle-bucket-status-error";

    @Output(NOTIFY_BUCKET_STATUS_ERROR)
    MessageChannel notifyBucketStatusError();

    String NOTIFY_BUCKET_EMPTY = "api-throttle-bucket-empty";

    /**
     * Notification sent when no token in bucket
     */
    @Output(NOTIFY_BUCKET_EMPTY)
    MessageChannel notifyBucketEmpty();

}

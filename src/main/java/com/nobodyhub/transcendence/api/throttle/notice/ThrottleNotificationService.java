package com.nobodyhub.transcendence.api.throttle.notice;

import com.nobodyhub.transcendence.api.throttle.notice.channel.ThrottleNotificationChannel;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(ThrottleNotificationChannel.class)
public class ThrottleNotificationService {
}

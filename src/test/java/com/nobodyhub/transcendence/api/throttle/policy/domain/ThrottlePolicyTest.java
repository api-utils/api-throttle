package com.nobodyhub.transcendence.api.throttle.policy.domain;

import org.junit.Test;
import org.testng.collections.Lists;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ThrottlePolicyTest {
    @Test
    public void compareToTest() {
        ThrottlePolicy policy1 = ThrottlePolicyBuilder.of("policy1").build();
        ThrottlePolicy policy2 = ThrottlePolicyBuilder.of("policy2").build();
        ThrottlePolicy policy3 = ThrottlePolicyBuilder.of("policy3").build();
        List<ThrottlePolicy> policies = Lists.newArrayList(policy2, policy1, policy3);
        Collections.sort(policies);
        assertEquals(3, policies.size());
        assertEquals(policy1, policies.get(0));
        assertEquals(policy2, policies.get(1));
        assertEquals(policy3, policies.get(2));
    }
}
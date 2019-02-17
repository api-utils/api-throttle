package com.nobodyhub.transcendence.api.throttle.policy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobodyhub.transcendence.api.throttle.api.domain.PagingResponse;
import com.nobodyhub.transcendence.api.throttle.api.domain.SingleResponse;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import com.nobodyhub.transcendence.api.throttle.policy.service.ThrottlePolicyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ThrottlePolicyController.class)
@ContextConfiguration(classes = ThrottlePolicyControllerTestConfiguration.class)
public class ThrottlePolicyControllerTest {
    @MockBean
    private ThrottlePolicyService policyService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<PagingResponse<ThrottlePolicy>> findAllPolicies;
    private JacksonTester<SingleResponse<ThrottlePolicy>> findByBucket;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void findAllPoliciesTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        PagingResponse<ThrottlePolicy> policies = PagingResponse.of(
                ThrottlePolicyBuilder.of("findAllPoliciesTest")
                        .nToken(100L)
                        .interval(2000L)
                        .window(new BucketWindow(10000, 10))
                        .build());
        // given
        given(this.policyService.findAll(pageable)).willReturn(policies);

        // when
        MockHttpServletResponse resp = mvc.perform(
                get("/throttle/policy/all"))
                .andReturn()
                .getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), resp.getStatus());
        assertEquals(findAllPolicies.write(policies).getJson(), resp.getContentAsString());
    }

    @Test
    public void findByBucketTest() throws Exception {
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("findByBucketTest")
                .nToken(100L)
                .interval(2000L)
                .window(new BucketWindow(10000, 10))
                .build();
        // given
        given(this.policyService.find("findByBucketTest"))
                .willReturn(policy);

        // then
        MockHttpServletResponse resp = mvc.perform(
                get("/throttle/policy//bucket/findByBucketTest"))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.OK.value(), resp.getStatus());
        assertEquals(findByBucket.write(SingleResponse.of(policy)).getJson(), resp.getContentAsString());
    }

}
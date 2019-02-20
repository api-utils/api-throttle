package com.nobodyhub.transcendence.api.throttle.bucket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.api.domain.ListResponse;
import com.nobodyhub.transcendence.api.throttle.bucket.controller.domain.BucketSearchParam;
import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.bucket.service.ThrottleBucketService;
import com.nobodyhub.transcendence.api.throttle.bucket.utils.BucketStatusBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.testng.AssertJUnit.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(ThrottleBucketController.class)
@ContextConfiguration(classes = ThrottleBucketControllerTestConfiguration.class)
public class ThrottleBucketControllerTest {
    @MockBean
    private ThrottleBucketService bucketService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<BucketSearchParam> paramTester;
    private JacksonTester<ListResponse<BucketStatus>> respTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void searchTest() throws Exception {
        BucketSearchParam param = new BucketSearchParam();
        param.setBuckets(Lists.newArrayList("s1", "s2", "s3"));
        BucketStatus s1 = BucketStatusBuilder.of("s1").build();
        BucketStatus s2 = BucketStatusBuilder.of("s2").build();
        BucketStatus s3 = BucketStatusBuilder.of("s3").build();
        List<BucketStatus> statuses = Lists.newArrayList(s1, s2, s3);

        given(this.bucketService.findBucket(Lists.newArrayList("s1", "s2", "s3")))
                .willReturn(statuses);
        MockHttpServletResponse resp = this.mvc.perform(post("/throttle/bucket/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramTester.write(param).getJson()))
                .andReturn()
                .getResponse();
        assertEquals(HttpStatus.OK.value(), resp.getStatus());
        assertEquals(this.respTester.write(ListResponse.of(statuses)).getJson(), resp.getContentAsString());
    }

}
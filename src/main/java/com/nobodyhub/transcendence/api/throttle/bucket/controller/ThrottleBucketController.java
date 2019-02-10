package com.nobodyhub.transcendence.api.throttle.bucket.controller;


import com.nobodyhub.transcendence.api.throttle.api.domain.ListResponse;
import com.nobodyhub.transcendence.api.throttle.bucket.controller.domain.BucketSearchParam;
import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.bucket.service.ThrottleBucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/throttle/bucket")
@RequiredArgsConstructor
public class ThrottleBucketController {
    private final ThrottleBucketService bucketService;

    @PostMapping(path = "/search")
    ListResponse<BucketStatus> search(@RequestBody BucketSearchParam param) {
        return ListResponse.of(bucketService.findBucket(param.getBuckets()));
    }
}
